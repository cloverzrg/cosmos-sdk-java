package com.jeongen.cosmos.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import cosmos.tx.v1beta1.ServiceOuterClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Yang Guanrong
 * @date 2019/08/31 17:23
 */
public class ProtoGsonUtil {

    public static <T> T toProto(Class<T> klass, String json) {
        return getGson(klass).fromJson(json, klass);
    }

    /**
     * 如果这个方法要设置为public方法，那么需要确定gson是否是一个不可变对象，否则就不应该开放出去
     *
     * @param messageClass
     * @param <E>
     * @return
     */
    private static <E> Gson getGson(Class<E> messageClass) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .registerTypeAdapter(ServiceOuterClass.GetTxResponse.class, new MessageAdapter(messageClass))
                .create();

        return gson;
    }

    private static class MessageAdapter<E extends Message> extends TypeAdapter<E> {

        private Class<E> messageClass;

        public MessageAdapter(Class<E> messageClass) {
            this.messageClass = messageClass;
        }

        @Override
        public void write(JsonWriter jsonWriter, E value) throws IOException {
            jsonWriter.jsonValue(JsonFormat.printer().print(value));
        }

        @Override
        public E read(JsonReader jsonReader) throws IOException {
            try {
                // 这里必须用范型<E extends Message>，不能直接用 Message，否则将找不到 newBuilder 方法
                Method method = messageClass.getMethod("newBuilder");
                // 调用静态方法
                E.Builder builder = (E.Builder) method.invoke(null);

                JsonParser jsonParser = new JsonParser();
                JsonFormat.parser().merge(jsonParser.parse(jsonReader).toString(), builder);
                return (E) builder.build();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
    }

}