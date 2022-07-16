package com.crypto.controller;

import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;
import com.crypto.model.TransferCryptoRequest;
import com.crypto.response.TransactionResponse;
import com.crypto.service.PriceListService;
import com.crypto.service.TransferCryptoTransactionService;
import com.crypto.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.crypto.constant.CommonAttribute.BTCUSDT;
import static com.crypto.constant.CommonAttribute.BUYACTION;
import static com.crypto.constant.CommonAttribute.ETHUSDT;
import static com.crypto.constant.CommonAttribute.SELLACTION;

@RestController
@Validated
public class ApiController {

    @Autowired
    WalletService walletService;

    @Autowired
    PriceListService priceListService;

    @Autowired
    TransferCryptoTransactionService transferCryptoTransactionService;


    @GetMapping("/api/wallet/{username}")
    public TransactionResponse getUserWallets(@PathVariable @NotNull final String username) throws NotFoundException {
        return TransactionResponse.builder()
                .status(HttpStatus.OK)
                .data(walletService.getAllWalletByUser(username))
                .build();
    }

    @GetMapping("/api/crypto/price/{symbol}")
    public TransactionResponse getPriceListBySymbol(@PathVariable @NotNull final String symbol) throws NotFoundException {
        return TransactionResponse.builder()
                .status(HttpStatus.OK)
                .data(priceListService.getPriceListBySymbol(symbol))
                .build();
    }

    @GetMapping("/api/crypto/prices")
    public TransactionResponse getAllPriceList() {
        return TransactionResponse.builder()
                .status(HttpStatus.OK)
                .data(priceListService.getAllPriceList())
                .build();
    }

    @GetMapping("/api/wallet/{username}/transactions")
    public TransactionResponse getUserTransactions(@PathVariable @NotNull final String username) {

        return TransactionResponse.builder()
                .status(HttpStatus.OK)
                .data(transferCryptoTransactionService.getUserTransactions(username))
                .build();
    }


    @PostMapping("/api/crypto/purchase")
    public TransactionResponse purchaseCrypto(@Valid @RequestBody TransferCryptoRequest transaction) throws NotFoundException, BadRequestException {

        checkAttribute(transaction);

        return TransactionResponse.builder()
                .status(HttpStatus.OK)
                .data(transferCryptoTransactionService.purchaseCryptoTransaction(transaction))
                .build();
    }

    private void checkAttribute(TransferCryptoRequest transaction) throws BadRequestException {
        String symbol = transaction.getSymbol();
        String action = transaction.getAction();

        if (!BTCUSDT.getValue().equalsIgnoreCase(symbol) && !ETHUSDT.getValue().equalsIgnoreCase(symbol)) {
            throw new BadRequestException("Invalid cryptocurrency");
        }

        if (!BUYACTION.getValue().equalsIgnoreCase(action) && !SELLACTION.getValue().equalsIgnoreCase(action)) {
            throw new BadRequestException("Invalid Action");
        }
    }

}
