package com.crypto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String payWallet;
    private String symbol;

    @Column(precision = 20, scale = 8)
    private BigDecimal purchasePrice;

    @Column(precision = 20, scale = 8)
    private BigDecimal quantity;

    private String currency;

    @Column(precision = 20, scale = 8)
    private BigDecimal totalAmount;

    @CreationTimestamp
    private Date created;

}
