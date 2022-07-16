package com.crypto.error;

public class NotFoundException extends Exception {
    public NotFoundException(String user_wallet_not_found) {
        super(user_wallet_not_found);
    }
}
