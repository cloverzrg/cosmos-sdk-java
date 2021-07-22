#!/bin/bash
cd src/main/java
javaPluginPath=~/bin/protoc-gen-grpc-java
commonParams="-I=proto --java_out=. --grpc-java_out=. --plugin=protoc-gen-grpc-java=${javaPluginPath}"
protoc $commonParams proto/cosmos/auth/v1beta1/*.proto
protoc $commonParams proto/cosmos/bank/v1beta1/*.proto
protoc $commonParams proto/cosmos/tx/v1beta1/*.proto
protoc $commonParams proto/cosmos/base/tendermint/v1beta1/query.proto