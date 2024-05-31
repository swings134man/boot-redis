package com.boot.redis.config.util;

import java.util.LinkedHashMap;

public class FieldMap extends LinkedHashMap<String, Object> {

    public static FieldMap of(String key, Object value) {
        FieldMap map = new FieldMap();
        map.put(key, value);
        return map;
    }

    public FieldMap add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
