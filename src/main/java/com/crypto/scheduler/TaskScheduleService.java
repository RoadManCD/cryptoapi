package com.crypto.scheduler;

import com.crypto.entity.PriceList;
import com.crypto.model.CryptoPriceList;
import com.crypto.repository.PriceListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
@Transactional
@Slf4j
public class TaskScheduleService {


    private final static String BTCUSDT = "BTCUSDT";
    private final static String ETHUSDT = "ETHUSDT";

    @Autowired
    private ThirdPartyDataService thirdpartyData;

    @Autowired
    private PriceListRepository priceListRepository;

    @Scheduled(fixedRate = 10000)
    public void getCryptos() {

        CompletableFuture<Map<String, CryptoPriceList>> binanceList = thirdpartyData.getBinanceCrypto();
        CompletableFuture<Map<String, CryptoPriceList>> huobiList = thirdpartyData.getHuobiCrypto();
        BigDecimal ethusdtAskPrice, btcusdtAskPrice, ethusdtBidPrice, btcusdtBidPrice;
        CryptoPriceList binanceETHUSDT = new CryptoPriceList();
        CryptoPriceList binanceBTCUSDT = new CryptoPriceList();
        CryptoPriceList huobiETHUSDT = new CryptoPriceList();
        CryptoPriceList huobiBTCUSDT = new CryptoPriceList();
        boolean huobiIsUp = false;
        boolean binanceIsUp = false;


        try {
            if (null != binanceList.get()) {

                binanceETHUSDT = binanceList.get().get(ETHUSDT);
                binanceBTCUSDT = binanceList.get().get(BTCUSDT);
                binanceIsUp = true;
            }
        } catch (Exception ex) {
            log.info("binanceList error : " + ex.getMessage());
            binanceETHUSDT = null;
            binanceBTCUSDT = null;
            binanceIsUp = false;
        }

        try {
            if (null != huobiList.get()) {
                huobiETHUSDT = huobiList.get().get(ETHUSDT);
                huobiBTCUSDT = huobiList.get().get(BTCUSDT);
                huobiIsUp = true;
            }
        } catch (Exception ex) {
            log.info("huobiList error : " + ex.getMessage());
            huobiETHUSDT = null;
            huobiBTCUSDT = null;
            huobiIsUp = false;
        }

        if (!binanceIsUp && !huobiIsUp) {
            // not updating price;
        } else {

            if (binanceIsUp && huobiIsUp) {
//                ethusdtAskPrice = huobiETHUSDT.getAskPrice().compareTo(binanceETHUSDT.getAskPrice()) > 0 ? huobiETHUSDT.getAskPrice() : binanceETHUSDT.getAskPrice();
//                btcusdtAskPrice = huobiBTCUSDT.getAskPrice().compareTo(binanceBTCUSDT.getAskPrice()) > 0 ? huobiBTCUSDT.getAskPrice() : binanceBTCUSDT.getAskPrice();
//                ethusdtBidPrice = huobiETHUSDT.getBidPrice().compareTo(binanceETHUSDT.getBidPrice()) < 0 ? huobiETHUSDT.getBidPrice() : binanceETHUSDT.getBidPrice();
//                btcusdtBidPrice = huobiBTCUSDT.getBidPrice().compareTo(binanceBTCUSDT.getBidPrice()) < 0 ? huobiBTCUSDT.getBidPrice() : binanceBTCUSDT.getBidPrice();
                ethusdtAskPrice = huobiETHUSDT.getAskPrice().add(binanceETHUSDT.getAskPrice()).divide(BigDecimal.valueOf(2));
                btcusdtAskPrice = huobiBTCUSDT.getAskPrice().add(binanceBTCUSDT.getAskPrice()).divide(BigDecimal.valueOf(2));
                ethusdtBidPrice = huobiETHUSDT.getBidPrice().add(binanceETHUSDT.getBidPrice()).divide(BigDecimal.valueOf(2));
                btcusdtBidPrice = huobiBTCUSDT.getBidPrice().add(binanceBTCUSDT.getBidPrice()).divide(BigDecimal.valueOf(2));

            } else if (!huobiIsUp) {
                ethusdtAskPrice = binanceETHUSDT.getAskPrice();
                btcusdtAskPrice = binanceBTCUSDT.getAskPrice();
                ethusdtBidPrice = binanceETHUSDT.getBidPrice();
                btcusdtBidPrice = binanceBTCUSDT.getBidPrice();
            } else {
                ethusdtAskPrice = huobiETHUSDT.getAskPrice();
                btcusdtAskPrice = huobiBTCUSDT.getAskPrice();
                ethusdtBidPrice = huobiETHUSDT.getBidPrice();
                btcusdtBidPrice = huobiBTCUSDT.getBidPrice();
            }

            priceListRepository.save(new PriceList(ETHUSDT, ethusdtBidPrice, ethusdtAskPrice, new Date()));
            priceListRepository.save(new PriceList(BTCUSDT, btcusdtBidPrice, btcusdtAskPrice, new Date()));
        }

    }


}
