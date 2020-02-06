package com.realoldroot.nginx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class JsonUtil {

    private static ObjectMapper MAPPER = new ObjectMapper();


    public static ObjectMapper getMAPPER() {
        return MAPPER;
    }


    @NotNull
    public static String writeJson(Object entity) {
        if (entity == null) {
            return "null";
        }
        String str = "";
        try {
            str = MAPPER.writeValueAsString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Nullable
    public static <T> T readJson(String jsonStr, Class<T> t) {
        T obj = null;
        try {
            obj = MAPPER.readValue(jsonStr, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static byte[] writeValueAsBytes(Object entity) throws JsonProcessingException {
        return MAPPER.writeValueAsBytes(entity);
    }

    public static <T> T readBytes(byte[] bytes, Class<T> clazz) throws IOException {
        return MAPPER.readValue(bytes, MAPPER.constructType(clazz));
    }

    public static <T> T readBytes(InputStream stream, Class<T> clazz) throws IOException {
        return MAPPER.readValue(stream, MAPPER.constructType(clazz));
    }

    public static <T> T readBytes(byte[] bytes, Type type) throws IOException {
        return MAPPER.readValue(bytes, MAPPER.constructType(type));
    }
}
