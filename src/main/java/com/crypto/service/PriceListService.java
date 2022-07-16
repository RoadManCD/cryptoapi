package com.crypto.service;

import com.crypto.entity.PriceList;
import com.crypto.error.NotFoundException;

import java.util.List;

public interface PriceListService {


    PriceList getPriceListBySymbol(final String symbol) throws NotFoundException;

    List<PriceList> getAllPriceList();

}
