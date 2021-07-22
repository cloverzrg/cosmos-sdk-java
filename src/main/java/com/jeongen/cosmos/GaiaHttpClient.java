package com.jeongen.cosmos;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.jeongen.cosmos.util.JsonToProtoObjectUtil;
import okhttp3.*;
import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class GaiaHttpClient {

    private String baseUrl;
    private static final JsonFormat.Parser parser = JsonToProtoObjectUtil.getParser();
    private static final Logger logger = LoggerFactory.getLogger(GaiaHttpClient.class);

    public GaiaHttpClient(String baseUrl) {
        this.baseUrl = baseUrl.trim();
    }


    public <T extends GeneratedMessageV3> T get(String path, Class<T> resClass) throws Exception {
        return invoke(path, "GET", null, null, resClass);
    }

    public <T extends GeneratedMessageV3> T post(String path, String body, Class<T> resClass) throws Exception {
        return invoke(path, "POST", body, null, resClass);
    }

    public <T extends GeneratedMessageV3> T get(String path, MultiValuedMap<String, String> queryMap, Class<T> resClass) throws Exception {
        return invoke(path, "GET", null, queryMap, resClass);
    }


    public <T extends GeneratedMessageV3> T invoke(String path, String method, String body, MultiValuedMap<String, String> queryMap, Class<T> resClass) throws Exception {
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .readTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(this.baseUrl).newBuilder();
        httpUrlBuilder.encodedPath(path);
        if (queryMap != null) {
            queryMap.keySet().forEach((key) -> {
                queryMap.get(key).forEach(val -> httpUrlBuilder.addQueryParameter(key, val));
            });
        }
        HttpUrl url = httpUrlBuilder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        switch (method) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(RequestBody.create(body, MediaType.parse("application/json; charset=utf-8")));
                break;
            default:
                throw new RuntimeException("unknown method");
        }
        try {
            Method getDefaultInstance = resClass.getDeclaredMethod("getDefaultInstance");
            T temp1 = (T) getDefaultInstance.invoke(resClass);
            Message.Builder builder = temp1.toBuilder();
            Request request = requestBuilder.build();
            Response response = httpClient.newCall(request).execute();
            assert response.body() != null;
            if (response.code() >= 400) {
                String msg = String.format("ATOM-HTTP code %s, res:%s", response.code(), response.body().string());
                throw new Exception(msg);
            }
            parser.merge(response.body().string(), builder);
            return (T) builder.build();
        } catch (Exception e) {
            // 未知异常
            logger.error("ATOM-API Exception  {} {}", e, method, url);
            throw new Exception(e);
        }

    }
}
