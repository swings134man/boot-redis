package com.boot.redis.util;

import com.boot.redis.config.util.VersionCheckUtil;
import com.kcthota.version4j.exceptions.VersionNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class VersionTest {

    @Test
    @DisplayName("1. Version Basic Use Test")
    void use_1() {
        String clientVersion = "2.0.0";
        String comparisonVersion = "1.5.0";

        Map<String, Object> resultMap = VersionCheckUtil.checkVersion(clientVersion, comparisonVersion);

        System.out.println("resultMap = " + resultMap);
    }

    @Test
    @DisplayName("2. Version Exception Use Test")
    void use_2() {
        String clientVersion = null;
        String comparisonVersion = "";
        String version = "1.0.0";
        String targetVersion = "100001.0.0"; // VersionNotValidException

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            VersionCheckUtil.checkVersion(clientVersion, comparisonVersion);
        });

        Assertions.assertThrows(VersionNotValidException.class, () -> {
            VersionCheckUtil.checkVersion(version, targetVersion);
        });
    }

    @Test
    @DisplayName("3. Version Special Case")
    void use_3() {
        String clientVersion = "2.0.0";
        String comparisonVersion = "error?";

        Map<String, Object> resultMap = VersionCheckUtil.checkVersion(clientVersion, comparisonVersion);

        System.out.println("resultMap = " + resultMap);
    }

}
