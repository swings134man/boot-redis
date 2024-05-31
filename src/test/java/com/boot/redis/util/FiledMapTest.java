package com.boot.redis.util;

import com.boot.redis.config.util.FieldMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class FiledMapTest {

    // FiledMapTest
    @Test
    @DisplayName("1. FiledMap Use Test")
    void use() {
        FieldMap map = FieldMap.of("one", "1")
                .add("two", 2L)
                .add("three", 3)
                .add("four", new byte[]{1, 2, 3, 4});

        // Type Check
        Assertions.assertTrue(map instanceof Map); // true
        Assertions.assertTrue(map instanceof LinkedHashMap); // true

        Assertions.assertTrue(map.get("one") instanceof String); // true
        Assertions.assertTrue(map.get("two") instanceof Long); // true
    }

}
