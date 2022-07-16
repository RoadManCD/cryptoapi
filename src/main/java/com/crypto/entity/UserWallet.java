package com.crypto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@IdClass(UserWalletId.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserWallet {

    @Id
    private String username;

    @Id
    private String currency;

    @Column(precision = 20, scale = 8)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date created;


    private Date lastUpdated;


}
