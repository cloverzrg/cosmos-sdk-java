# Cosmos SDK for Java

The SDK integrates and implement all the functions required by the exchange to access Cosmos (ATOM). If you need additional support for other API, please submit an issue. 
0.0.3 support Gaia v7.0.0, but not test yet. 
- Scan chain
- Address generation and verification
- Transaction generation, signature, broadcast
- Query tx detail
- Query account detail
- Query balance
- Estimate gas

### Usage
```xml
<dependency>
    <groupId>com.jeongen.cosmos</groupId>
    <artifactId>sdk</artifactId>
    <version>0.0.3</version>
</dependency>
```

### Rest API

```java
import com.jeongen.cosmos.crypro.CosmosCredentials;
import com.jeongen.cosmos.vo.SendInfo;
import cosmos.base.abci.v1beta1.Abci;
import junit.framework.TestCase;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CosmosRestApiClientTest extends TestCase {

    public void testSendMultiTx() throws Exception {
        CosmosRestApiClient gaiaApiService = new CosmosRestApiClient("https://api.cosmos.network", "cosmoshub-4", "uatom");

        byte[] privateKey = Hex.decode("c2ad7a31c06ea8bb560a0467898ef844523f2f804dec96fedf65906dbb951f24");
        CosmosCredentials credentials = CosmosCredentials.create(privateKey);
        // generate address
        System.out.println("address:" + credentials.getAddress());
        List<SendInfo> sendList = new ArrayList<>();
        // add a send message
        SendInfo sendMsg1 = SendInfo.builder()
                .credentials(credentials)
                .toAddress("cosmos12kd7gu4lamw29pv4u6ug8aryr0p7wm207uwt30")
                .amountInAtom(new BigDecimal("0.0001"))
                .build();
        sendList.add(sendMsg1);
        // add a send message
        SendInfo sendMsg2 = SendInfo.builder()
                .credentials(credentials)
                .toAddress("cosmos1u3zluamfx5pvgha0dn73ah4pyu9ckv6scvdw72")
                .amountInAtom(new BigDecimal("0.0001"))
                .build();
        sendList.add(sendMsg2);
        // build、sign、broadcast transactions
        Abci.TxResponse txResponse = gaiaApiService.sendMultiTx(credentials, sendList, new BigDecimal("0.000001"), 200000);
        System.out.println(txResponse);

        // query send tx by height
        ServiceOuterClass.GetTxsEventResponse txsEventByHeight = cosmosRestApiClient.getTxsEventByHeight(6900000L, "");
    }
}
```


### gRPC
```java
import cosmos.bank.v1beta1.QueryGrpc;
import cosmos.bank.v1beta1.QueryOuterClass;
import cosmos.tx.v1beta1.ServiceGrpc;
import cosmos.tx.v1beta1.ServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Main {
    
    public void grpcDemo() {
        // grpc endpoint
        ManagedChannel channel = ManagedChannelBuilder.forTarget("127.0.0.1:9090")
                .usePlaintext()
                .build();

        // new query service
        QueryGrpc.QueryBlockingStub queryBlockingStub = QueryGrpc.newBlockingStub(channel);

        // query the balance
        QueryOuterClass.QueryAllBalancesResponse balance = queryBlockingStub.allBalances(QueryOuterClass.QueryAllBalancesRequest.newBuilder().setAddress("cosmos1wjuh6ee7gzkr489pmfc3qcw6qvjensquzv3s0x").build());
        System.out.println(balance.getBalances(0).getAmount());

        // new transactions service
        ServiceGrpc.ServiceBlockingStub txServiceBlockingStub = ServiceGrpc.newBlockingStub(channel);
        // query tx detail
        ServiceOuterClass.GetTxResponse txResponse = txServiceBlockingStub.getTx(ServiceOuterClass.GetTxRequest.newBuilder().setHash("9A500E826296559DEF05E9D0C7D354F05A37FCF69DDD79B78EF0FCA438E8660B").build());
        System.out.println(txResponse.getTxResponse().getTxhash());

        // broadcast transactions
        ServiceOuterClass.BroadcastTxResponse broadcastTxResponse = txServiceBlockingStub.broadcastTx(ServiceOuterClass.BroadcastTxRequest.newBuilder().build());
        System.out.println(broadcastTxResponse.getTxResponse().getTxhash());
    }
    
}

```
