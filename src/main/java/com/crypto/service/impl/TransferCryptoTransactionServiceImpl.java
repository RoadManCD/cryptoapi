package com.crypto.service.impl;

import com.crypto.entity.PriceList;
import com.crypto.entity.Transaction;
import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;
import com.crypto.model.TransferCryptoRequest;
import com.crypto.repository.TransactionRepository;
import com.crypto.service.PriceListService;
import com.crypto.service.TransferCryptoTransactionService;
import com.crypto.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static com.crypto.constant.CommonAttribute.USDT;

@Service
@Transactional
public class TransferCryptoTransactionServiceImpl implements TransferCryptoTransactionService {

    @Autowired
    PriceListService priceListService;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;


    public List<Transaction> getUserTransactions(final String username) {
        return transactionRepository.findAllByUsername(username);
    }

    //assume BUY action : pay USDT get BTC/ETH , SELL action : pay BTC/ETH get USDT
    @Transactional(rollbackOn = Exception.class)
    public String purchaseCryptoTransaction(final TransferCryptoRequest transaction) throws NotFoundException, BadRequestException {
        String symbol = transaction.getSymbol().toUpperCase();
        PriceList priceList = priceListService.getPriceListBySymbol(symbol);

        String action = transaction.getAction();
        String username = transaction.getUsername();
        BigDecimal purchasePrice = transaction.getPurchasePrice();
        BigDecimal quantity = transaction.getQuantity();
        String cryptoCurrency = symbol.replace(USDT.getValue(), "");

        String payCurrency, getCurrency;
        BigDecimal earnAmount, deductAmount;

        if (action.equalsIgnoreCase("buy")) {
            payCurrency = USDT.getValue();
            getCurrency = cryptoCurrency;
            deductAmount = purchasePrice.multiply(quantity);
            earnAmount = quantity;
            checkPrice(purchasePrice, priceList.getBuyPrice(), -1, cryptoCurrency);

        } else {
            payCurrency = cryptoCurrency;
            getCurrency = USDT.getValue();
            deductAmount = quantity;
            earnAmount = purchasePrice.multiply(quantity);
            checkPrice(purchasePrice, priceList.getSellPrice(), 1, cryptoCurrency);
        }

        walletService.saveByAddAmount(username, getCurrency, earnAmount);
        walletService.saveByDeductAmount(username, payCurrency, deductAmount);
        Transaction newTransaction = Transaction.builder().username(username)
                .payWallet(payCurrency)
                .symbol(symbol)
                .purchasePrice(purchasePrice)
                .quantity(quantity)
                .totalAmount(earnAmount)
                .currency(getCurrency)
                .build();

        transactionRepository.save(newTransaction);

        return "Traded action : " + action + " cryptocurrency " + cryptoCurrency + " at price " + purchasePrice +  "  transaction successful";

    }

    private void checkPrice(BigDecimal purchasePrice, BigDecimal currentPrice, int result, String currency) throws BadRequestException {
        String valueString = result > 0 ? "higher" : "lower";
        if (purchasePrice.compareTo(currentPrice) == result) {
            throw new BadRequestException(currency + " purchase price " + purchasePrice + " is " + valueString + " than current best price "
                    + currentPrice + ", unable to trade");
        }
    }
}
