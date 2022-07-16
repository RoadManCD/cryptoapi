package com.crypto.service.impl;

import com.crypto.entity.UserWallet;
import com.crypto.error.BadRequestException;
import com.crypto.error.NotFoundException;
import com.crypto.repository.UserWalletRepository;
import com.crypto.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    @Autowired
    UserWalletRepository userWalletRepository;

    public List<UserWallet> getAllWalletByUser(final String username) throws NotFoundException {

        List<UserWallet> userWallets = userWalletRepository.findUserWalletByUsername(username);

        if (CollectionUtils.isEmpty(userWallets)) {
            throw new NotFoundException("User wallet not found");
        }

        return userWallets;

    }

    @Override
    public UserWallet getUserWalletByUserAndCurrency(String username, String currency) throws NotFoundException {
        UserWallet userWallet = userWalletRepository.findByUsernameAndCurrency(username, currency);
        return userWallet;
    }

    @Override
    public void saveByDeductAmount(String username, String currency, BigDecimal totalAmount) throws NotFoundException, BadRequestException {

        UserWallet userWallet = getUserWalletByUserAndCurrency(username, currency);

        if (userWallet == null) userWallet = UserWallet.builder()
                .username(username)
                .currency(currency)
                .amount(BigDecimal.ZERO)
                .created(new Date())
                .lastUpdated(new Date())
                .build();

        BigDecimal newAmount = userWallet.getAmount().subtract(totalAmount);

        if (newAmount.compareTo(BigDecimal.ZERO) == -1) {
            throw new BadRequestException("insuffient fund!  wallet " + currency + " available : " + userWallet.getAmount());
        }

        userWallet.setAmount(newAmount);
        userWalletRepository.save(userWallet);
    }

    @Override
    public void saveByAddAmount(String username, String currency, BigDecimal quantity) throws NotFoundException {
        UserWallet userWallet = getUserWalletByUserAndCurrency(username, currency);
        if (userWallet == null) {
            userWallet = UserWallet.builder()
                    .username(username)
                    .currency(currency)
                    .amount(quantity)
                    .created(new Date())
                    .lastUpdated(new Date())
                    .build();
        } else {
            BigDecimal newAmount = userWallet.getAmount().add(quantity);
            userWallet.setAmount(newAmount);
        }

        userWalletRepository.save(userWallet);
    }


}
