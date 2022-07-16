package com.crypto.service.impl;

import com.crypto.entity.PriceList;
import com.crypto.entity.Transaction;
import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;
import com.crypto.model.TransferCryptoTransaction;
import com.crypto.repository.TransactionRepository;
import com.crypto.service.PriceListService;
import com.crypto.service.TransferCryptoTransactionService;
import com.crypto.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransferCryptoTransactionServiceImpl implements TransferCryptoTransactionService {

    @Autowired
    PriceListService priceListService;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;

    private static final String USDT = "USDT";

    public List<Transaction> getUserTransactions(final String username) {
        return transactionRepository.findAllByUsername(username);
    }

    //assume BUY action : pay USDT get BTC/ETH , SELL action : pay BTC/ETH get USDT
    public String purchaseCryptoTransaction(final TransferCryptoTransaction transaction) throws NotFoundException, BadRequestException {
        String symbol = transaction.getSymbol().toUpperCase();
        PriceList priceList = priceListService.getPriceListBySymbol(symbol);

        String action = transaction.getAction();
        String username = transaction.getUsername();
        BigDecimal purchasePrice = transaction.getPurchasePrice();
        BigDecimal quantity = transaction.getQuantity();
        String cryptoCurrency = symbol.replace(USDT, "");

        String payCurrency, getCurrency;
        BigDecimal earnAmount, deductAmount;

        if (action.equalsIgnoreCase("buy")) {
            payCurrency = USDT;
            getCurrency = cryptoCurrency;
            deductAmount = purchasePrice.multiply(quantity);
            earnAmount = quantity;

            if (purchasePrice.compareTo(priceList.getBuyPrice()) < 0) {
                throw new BadRequestException(cryptoCurrency + " purchase price " + purchasePrice + " is lower than current best price "
                        + priceList.getBuyPrice() + ", unable to buy");
            }

        } else {
            payCurrency = cryptoCurrency;
            getCurrency = USDT;
            deductAmount = quantity;
            earnAmount = purchasePrice.multiply(quantity);

            if (priceList.getSellPrice().compareTo(purchasePrice) < 0) {
                throw new BadRequestException(cryptoCurrency + " purchase price " + purchasePrice + " is higher than  current best price "
                        + priceList.getSellPrice() + ", unable to sell");
            }
        }

        walletService.saveByDeductAmount(username, payCurrency, deductAmount);
        walletService.saveByAddAmount(username, getCurrency, earnAmount);
        Transaction newTransaction = Transaction.builder().username(username)
                .payWallet(payCurrency)
                .symbol(symbol)
                .purchasePrice(purchasePrice)
                .quantity(quantity)
                .totalAmount(earnAmount)
                .currency(getCurrency)
                .build();

        transactionRepository.save(newTransaction);

        return "purchase successful";

    }
}
