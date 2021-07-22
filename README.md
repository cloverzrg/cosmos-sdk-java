### Cosmos SDK for Java

### 使用 (暂未发布，NOT Publish Yet)
```xml
<dependency>
    <groupId>com.jeongen.cosmos</groupId>
    <artifactId>sdk</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 使用 Rest API 



### 使用 gRPC 示例
```java
import com.jeongen.cosmos.GaiaApiService;
import cosmos.bank.v1beta1.QueryGrpc;
import cosmos.bank.v1beta1.QueryOuterClass;
import cosmos.tx.v1beta1.ServiceGrpc;
import cosmos.tx.v1beta1.ServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public void grpcDemo() {
    // grpc endpoint
    ManagedChannel channel = ManagedChannelBuilder.forTarget("127.0.0.1:9090")
            .usePlaintext()
            .build();

    // new query service
    QueryGrpc.QueryBlockingStub queryBlockingStub = QueryGrpc.newBlockingStub(channel);

    // query balance
    QueryOuterClass.QueryAllBalancesResponse balance = queryBlockingStub.allBalances(QueryOuterClass.QueryAllBalancesRequest.newBuilder().setAddress("cosmos1wjuh6ee7gzkr489pmfc3qcw6qvjensquzv3s0x").build());
    System.out.println(balance.getBalances(0).getAmount());

    // new transactions service
    ServiceGrpc.ServiceBlockingStub txServiceBlockingStub = ServiceGrpc.newBlockingStub(channel);
    // query tx
    ServiceOuterClass.GetTxResponse txResponse = txServiceBlockingStub.getTx(ServiceOuterClass.GetTxRequest.newBuilder().setHash("9A500E826296559DEF05E9D0C7D354F05A37FCF69DDD79B78EF0FCA438E8660B").build());
    System.out.println(txResponse.getTxResponse().getTxhash());

    // broadcast transactions
    ServiceOuterClass.BroadcastTxResponse broadcastTxResponse = txServiceBlockingStub.broadcastTx(ServiceOuterClass.BroadcastTxRequest.newBuilder().build());
    System.out.println(broadcastTxResponse.getTxResponse().getTxhash());
}
```