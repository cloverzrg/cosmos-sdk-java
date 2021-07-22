package com.jeongen.cosmos;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.jeongen.cosmos.util.JsonToProtoObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class GaiaHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(GaiaHttpClient.class);
    private static final JsonFormat.Parser parser = JsonToProtoObjectUtil.getParser();
    private String baseUrl;

    public GaiaHttpClient(String baseUrl) {
        this.baseUrl = baseUrl.trim();
    }

    public <T extends GeneratedMessageV3> T get(String path, Class<T> resClass) {
        return invoke(path, HttpMethod.GET, null, null, resClass);
    }

    public <T extends GeneratedMessageV3> T post(String path, String body, Class<T> resClass) {
        return invoke(path, HttpMethod.POST, body, null, resClass);
    }

    public <T extends GeneratedMessageV3> T get(String path, MultiValueMap<String, String> queryMap, Class<T> resClass) {
        return invoke(path, HttpMethod.GET, null, queryMap, resClass);
    }

    public <T extends GeneratedMessageV3> T invoke(String path, HttpMethod method, String body, MultiValueMap<String, String> queryMap, Class<T> resClass) {
        RestTemplate httpClient = new RestTemplateBuilder()
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();

        HttpEntity<String> req = new HttpEntity<>(body, getHeaders());
        URI uri = UriComponentsBuilder.fromHttpUrl(this.baseUrl).path(path).queryParams(queryMap).build().toUri();
        try {
            Method getDefaultInstance = resClass.getDeclaredMethod("getDefaultInstance");
            T temp1 = (T) getDefaultInstance.invoke(resClass);
            Message.Builder builder = temp1.toBuilder();
            ResponseEntity<String> responseEntity = httpClient.exchange(uri, method, req, String.class);
            parser.merge(responseEntity.getBody(), builder);
            return (T) builder.build();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 400-500 状态码异常
            String bodyAsString = e.getResponseBodyAsString();
            logger.error("ATOM-API error {} {} {}", e, method.name(), uri, bodyAsString);
            throw new RuntimeException(e);
        } catch (Exception e) {
            // 未知异常
            logger.error("ATOM-API Exception  {} {}", e, method.name(), uri);
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        return requestHeaders;
    }
}