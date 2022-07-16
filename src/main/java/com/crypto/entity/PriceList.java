package com.crypto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class PriceList {

    @Id
    private String symbol;
    @Column(precision = 20, scale = 8)
    private BigDecimal buyPrice;
    @Column(precision = 20, scale = 8)
    private BigDecimal sellPrice;

    private Date lastUpdate;

}
