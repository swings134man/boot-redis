package com.boot.redis.config.util;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//출처: https://gist.github.com/btnguyen2k/3716451#file-dpathutils-java
public class MapPathUtil {
    private final static Pattern PATTERN_INDEX = Pattern.compile("^\\[(\\d+)\\]$");
    private Object target;

    private MapPathUtil(Object target) {
        this.target = target;
        validateTarget();
    }

    public static MapPathUtil with(Object target) {
        return new MapPathUtil(target);
    }

    public MapPathUtil get(String dPath) {
        String[] paths = dPath.split("\\.");
        for (String path : paths) {
            target = extractValue(path);
        }
        return this;
    }

    public MapPathUtil findFirst(Predicate<Map<?, ?>> predicate) {
        validateTarget();
        if (target instanceof List<?>) {
            for (Map<?, ?> mo : (List<Map<?, ?>>) target) {
                if (predicate.test(mo)) {
                    target = mo;
                    return this;
                }
            }
        }
        throw new IllegalArgumentException("on MapPathUtil");
    }

    public Object complete() {
        return target;
    }

    private Object extractValue(String index) {
        validateTarget();
        Matcher m = PATTERN_INDEX.matcher(index);
        if (m.matches()) {
            int i = Integer.parseInt(m.group(1));
            if (target instanceof Object[]) {
                return ((Object[]) target)[i];
            }
            if (target instanceof List<?>) {
                return ((List<?>) target).get(i);
            }
        }
        if (target instanceof Map<?, ?> && ((Map<?, ?>) target).containsKey(index)) {
            return ((Map<?, ?>) target).get(index);
        }
        throw new IllegalArgumentException("on MapPathUtil");
    }

    private void validateTarget() {
        if (target == null) {
            throw new IllegalArgumentException("on MapPathUtil");
        }
    }
}
