package com.jeongen.cosmos.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CoinInSummarize {

    private String userId;
    private BigDecimal amount;

    private Set<String> fromAddress;

    private CoinInSummarize() {
    }

    public CoinInSummarize(String userId, BigDecimal amount, String from) {
        this.userId = userId;
        this.amount = amount;
        this.fromAddress = new HashSet<>();
        this.fromAddress.add(from);
    }

    public void addAmount(BigDecimal amount, String from) {
        this.amount = this.amount.add(amount);
        this.fromAddress.add(from);
    }
}
