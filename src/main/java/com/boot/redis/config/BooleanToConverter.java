package com.boot.redis.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N"; // Attribute가 null이 아니고 true일 경우 Y, 아닐 경우 N
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }
}
