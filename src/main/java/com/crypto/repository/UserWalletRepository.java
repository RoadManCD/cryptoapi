package com.crypto.repository;

import com.crypto.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, String> {

    List<UserWallet> findUserWalletByUsername(String username);

    UserWallet findByUsernameAndCurrency(String username, String currency);
}
