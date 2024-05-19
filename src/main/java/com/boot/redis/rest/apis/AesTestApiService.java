package com.boot.redis.rest.apis;

import com.boot.redis.config.properties.ApiGenProperties;
import com.boot.redis.config.util.AES256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AesTestApiService {

    private final ApiGenProperties.Api apiGenProperties;

    protected final String AES_KEY;
    protected final String IV;

    public AesTestApiService(ApiGenProperties apiGenProperties) {
        this.apiGenProperties = apiGenProperties.getApi();
        this.AES_KEY = this.apiGenProperties.getEncryptKey();
        this.IV = this.apiGenProperties.getIvKey();
    }

    // ---------------------------------- Constructors ----------------------------------

    public String encryptString(String args) throws Exception {
        log.info("Before encrypt: {}", args);

        String encrypt = encrypt(args);
        log.info("After encrypt: {}", encrypt);
        return encrypt;
    }

    public String decryptString(String args) throws Exception {
        log.info("Before decrypt: {}", args);

        String decrypt = decrypt(args);
        log.info("After decrypt: {}", decrypt);
        return decrypt;
    }


    private String encrypt(String arg) throws Exception {
        return AES256Util.encrypt(AES_KEY, IV, arg);
    }

    private String decrypt(String arg) throws Exception {
        return AES256Util.decrypt(AES_KEY, IV, arg);
    }
}
