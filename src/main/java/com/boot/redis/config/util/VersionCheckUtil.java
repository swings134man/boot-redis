package com.boot.redis.config.util;

import com.kcthota.version4j.models.Version;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for version check.
 */
public class VersionCheckUtil {


    public static Map<String, Object> checkVersion(String clientVersion, String comparisonVersion) {
        Map<String, Object> resultMap = new HashMap<>();

        // 1. Check if the client version is null or empty.
        if(StringUtils.isEmpty(clientVersion)) {
            throw new IllegalArgumentException("Client version is null or empty.");
        }

        // 2. Check if the comparison version is null or empty.
        if(StringUtils.isEmpty(comparisonVersion)) {
            throw new IllegalArgumentException("Comparison version is null or empty.");
        }

        // 3. Compare the client version and the comparison version.
        Version client = new Version(clientVersion);
        Version comparison = new Version(comparisonVersion);
        boolean result = comparison.greaterThan(client);

        // 4. Set the result to the map.
        resultMap.put("result", result);
        resultMap.put("clientVersion", clientVersion);
        resultMap.put("comparisonVersion", comparisonVersion);
        resultMap.put("message", result ? "comparison version is Greater" : "client version is Greater or Version Check Failed");

        return resultMap;
    }

}
