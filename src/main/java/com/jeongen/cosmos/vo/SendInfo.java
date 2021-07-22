package com.jeongen.cosmos.vo;

import com.jeongen.cosmos.crypro.CosmosCredentials;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SendInfo {
    CosmosCredentials credentials;
    String toAddress;
    BigDecimal amountInAtom;
}
