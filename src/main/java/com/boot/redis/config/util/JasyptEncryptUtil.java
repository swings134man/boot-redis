package com.boot.redis.config.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JasyptEncryptUtil {

    private static StringEncryptor stringEncryptor;

    @Autowired
    public JasyptEncryptUtil(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    /**
     * String Encrypt
     * @param data
     * @return String
     */
    public static String encrypt(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return stringEncryptor.encrypt(data);
    }

    /**
     *
     * @param data
     * @return String
     */
    public static String decrypt(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return stringEncryptor.decrypt(data);
    }

    /**
     * Map to String Encrypt
     * @param map
     * @return
     */
    public static String mapToStringEncrypt(Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";
        try {
            String json = objectMapper.writeValueAsString(map);
            result = encrypt(json);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * String Encrypt to Map Type Decrypt
     * @param data
     * @return
     */
    public static Map<String, Object> mapToStringDecrypt(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;

        String decrypt = decrypt(data);
        try {
            map = objectMapper.readValue(decrypt, Map.class);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
