package com.crypto.error;

public class BadRequestException extends Exception {
    public BadRequestException(String insuffient_fund) {
        super(insuffient_fund);
    }
}
