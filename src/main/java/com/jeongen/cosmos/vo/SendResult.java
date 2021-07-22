package com.jeongen.cosmos.vo;

import cosmos.base.abci.v1beta1.Abci;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SendResult {
    private Abci.TxResponse txResponse;
    private BigDecimal sendAmount;
    private BigDecimal gasFee;

    @Override
    public String toString() {
        return "SendResult{" +
                "txResponse=" + txResponse +
                ", sendAmount=" + sendAmount +
                ", gasFee=" + gasFee +
                '}';
    }
}
