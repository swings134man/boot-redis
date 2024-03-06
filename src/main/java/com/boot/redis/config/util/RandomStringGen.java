package com.boot.redis.config.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Random;

// Random String Generator
@Slf4j
public class RandomStringGen {

    private static final String CHAR_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_DIGITS = "0123456789";
    private static final String CHAR_SPECIAL = "!@#$%^&*";

    private static final String ALL_CHARS = CHAR_UPPERCASE + CHAR_LOWERCASE + CHAR_DIGITS + CHAR_SPECIAL;

    private static final int keyLength = 32;

    public static String generateRandomKey() {
        StringBuilder sb = new StringBuilder(keyLength);
        Random random = new SecureRandom();
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(ALL_CHARS.length());
            sb.append(ALL_CHARS.charAt(randomIndex));
        }

        log.info("Generated Key : {}", sb.toString());

        return sb.toString();
    }

}
