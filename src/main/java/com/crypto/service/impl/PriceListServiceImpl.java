package com.crypto.service.impl;

import com.crypto.entity.PriceList;
import com.crypto.error.NotFoundException;
import com.crypto.repository.PriceListRepository;
import com.crypto.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceListServiceImpl implements PriceListService {
    @Autowired
    PriceListRepository priceListRepository;

    public PriceList getPriceListBySymbol(final String symbol) throws NotFoundException {

        Optional<PriceList> priceList = priceListRepository.findById(symbol);

        if (!priceList.isPresent()) {
            throw new NotFoundException("Crypto price list not found : " + symbol);
        }

        return priceList.get();

    }

    @Override
    public List<PriceList> getAllPriceList() {
        return priceListRepository.findAll();
    }
}
