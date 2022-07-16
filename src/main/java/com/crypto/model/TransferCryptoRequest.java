package com.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferCryptoRequest {

    @NotNull(message = "symbol cannot be empty")
    private String symbol;

    @NotNull(message = "username cannot be empty")
    private String username;

    @NotNull(message = "action cannot be empty")
    private String action;

    @NotNull(message = "purchasePrice cannot be empty")
    @DecimalMin(value = "0.00000001")
    private BigDecimal purchasePrice;

    @NotNull(message = "quantity cannot be empty")
    @DecimalMin(value = "0.00000001")
    private BigDecimal quantity;
}
