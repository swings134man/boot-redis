package com.boot.redis.config.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static TypeReference<FieldMap> typeRef = new TypeReference<FieldMap>() {};

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper(); // ObjectMapper 인스턴스 초기화
    }

    @Inject
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper; // NOSONAR
    }

    public static ObjectMapper getMapper() {
        return JsonUtil.mapper;
    }

    public static FieldMap toMap(String json) throws IOException {
        return mapper.readValue(json, typeRef);
    }

    public static FieldMap toMap(Object object) {
        return mapper.convertValue(object, typeRef);
    }

    public static <T> T toObject(String json, TypeReference<T> typeRef) throws IOException {
        return mapper.readValue(json, typeRef);
    }

    public static <T> T toObject(Object object, TypeReference<T> typeRef) {
        return mapper.convertValue(object, typeRef);
    }

    public static <T> T toObject(Map<?, ?> map, Class<T> toValueType) {
        return mapper.convertValue(map, toValueType);
    }

    public static <T> T toObject(String json, Class<T> toValueType) throws IOException {
        return mapper.readValue(json, toValueType);
    }


    public static <T> List<T> toListObject(String json, Class<T> toValueType) throws IOException {
        JavaType type = JsonUtil.getMapper().getTypeFactory().constructCollectionType(List.class, toValueType);
        return mapper.readValue(json, type);
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
