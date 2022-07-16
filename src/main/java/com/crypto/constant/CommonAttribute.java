package com.crypto.constant;

public enum CommonAttribute {
    BTCUSDT("BTCUSDT"),
    ETHUSDT("ETHUSDT"),
    BUYACTION("BUY"),
    SELLACTION("SELL"),
    USDT("USDT");

    private final String value;

    CommonAttribute(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return this.value + " " + this.name();
    }
}
