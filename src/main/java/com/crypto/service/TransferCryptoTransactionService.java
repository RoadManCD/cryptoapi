package com.crypto.service;

import com.crypto.entity.Transaction;
import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;
import com.crypto.model.TransferCryptoTransaction;

import java.util.List;

public interface TransferCryptoTransactionService {

    List<Transaction> getUserTransactions(final String username);

    String purchaseCryptoTransaction(final TransferCryptoTransaction transaction) throws NotFoundException, BadRequestException;
}
