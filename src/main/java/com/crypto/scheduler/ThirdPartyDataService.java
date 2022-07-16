package com.crypto.scheduler;

import com.crypto.constant.CommonAttribute;
import com.crypto.model.Binance;
import com.crypto.model.CryptoPriceList;
import com.crypto.model.Huobi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.crypto.constant.CommonAttribute.BTCUSDT;
import static com.crypto.constant.CommonAttribute.ETHUSDT;

@Service
@Slf4j
public class ThirdPartyDataService {

    @Value("${resttemplate.request.timeout}")
    private int TIMEOUT ;

    @Value("${binance.url}")
    private String binanceUrl ;

    @Value("${houbi.url}")
    private String houbiUrl ;

    private final static ObjectMapper mapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(TIMEOUT)).setReadTimeout(Duration.ofMillis(TIMEOUT)).build();


    @Async("asyncExecutor")
    public CompletableFuture<Map<String, CryptoPriceList>> getBinanceCrypto() {
        log.info("start binance");
        List<Binance> binanceObject = (List<Binance>) restTemplate.getForObject(binanceUrl, List.class);

        List<Binance> listOfSymbol = mapper.convertValue(binanceObject, new TypeReference<List<Binance>>() {
        });
        Map<String, CryptoPriceList> cryptoPriceList = new HashMap<>();

        listOfSymbol.stream()
                .filter(c -> c.getSymbol().equalsIgnoreCase( ETHUSDT.getValue()) || c.getSymbol().equalsIgnoreCase( BTCUSDT.getValue()) )
                .forEach(c -> cryptoPriceList.put(c.getSymbol().toUpperCase(),
                        new CryptoPriceList(c.getSymbol().toUpperCase(), c.getBidPrice(), c.getAskPrice())));
        log.info("end getBinanceCrypto " + cryptoPriceList.toString() + " | timenow : " + new Date());

        return CompletableFuture.completedFuture(cryptoPriceList);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Map<String, CryptoPriceList>> getHuobiCrypto() {
        log.info("start getHuobiCrypto");
        Map<String, List<Huobi>> houbiObject = (Map<String, List<Huobi>>) restTemplate.getForObject(houbiUrl, Map.class);

        List<Huobi> listOfSymbol = mapper.convertValue(houbiObject.get("data"), new TypeReference<List<Huobi>>() {
        });
        Map<String, CryptoPriceList> cryptoPriceList = new HashMap<>();

        listOfSymbol.stream()
                .filter(c -> c.getSymbol().equalsIgnoreCase( ETHUSDT.getValue()) || c.getSymbol().equalsIgnoreCase( BTCUSDT.getValue() ))
                .forEach(c -> cryptoPriceList.put(c.getSymbol().toUpperCase(),
                        new CryptoPriceList(c.getSymbol().toUpperCase(), c.getBid(), c.getAsk())));

        log.info("end getHuobiCrypto : " + cryptoPriceList.toString() + " | timenow : " + new Date());

        return CompletableFuture.completedFuture(cryptoPriceList);
    }
}
