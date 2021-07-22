package com.jeongen.cosmos.crypro;

import com.jeongen.cosmos.util.AddressUtil;
import lombok.Getter;
import org.bitcoinj.core.ECKey;

@Getter
public class CosmosCredentials {

    private ECKey ecKey;
    private String address;

    private CosmosCredentials() {

    }

    public static CosmosCredentials create(ECKey ecKey) {
        CosmosCredentials credentials = new CosmosCredentials();
        credentials.ecKey = ecKey;
        credentials.address = AddressUtil.ecKeyToAddress(ecKey);
        return credentials;
    }

    public static CosmosCredentials create(byte[] privateKey) {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        return create(ecKey);
    }

}
