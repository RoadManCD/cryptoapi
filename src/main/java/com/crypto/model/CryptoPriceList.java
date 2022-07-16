package com.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CryptoPriceList {

    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
}
