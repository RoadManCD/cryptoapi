package com.crypto.service;

import com.crypto.entity.UserWallet;
import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<UserWallet> getAllWalletByUser(final String username) throws NotFoundException;

    UserWallet getUserWalletByUserAndCurrency(final String username, final String currency) throws NotFoundException;

    void saveByDeductAmount(String username, String payCurrency, BigDecimal totalAmount) throws NotFoundException, BadRequestException;

    void saveByAddAmount(String username, String getCurrency, BigDecimal quantity) throws NotFoundException;
}
