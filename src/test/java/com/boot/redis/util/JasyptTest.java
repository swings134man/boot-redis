package com.boot.redis.util;

import com.boot.redis.config.util.JasyptEncryptUtil;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class JasyptTest {

    @Test
    @DisplayName("1. Encrypt Test")
    void test1() {
        String secretKey = "ThisIsTestSecretKey";

        String targetText = "This Is Target Text!!";

        // Using Jasypt
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES"); //Default

        String encryptedText = encryptor.encrypt(targetText);
        System.out.println("encryptedText = " + encryptedText);

        String decryptedText = encryptor.decrypt(encryptedText);
        System.out.println("decryptedText = " + decryptedText);

        Assertions.assertThat(decryptedText).isEqualTo(targetText);
    }

    @Test
    @DisplayName("2. Encrypt Test Wrongs")
    void test2() {
        String secretKey = "mySecretKey";
        String wrongKey = "wrongKey";
        String originalText = "This is a secret message";

        // 암호화
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        String encryptedText = encryptor.encrypt(originalText);

        // 다른 비밀번호로 복호화 시도
        StandardPBEStringEncryptor wrongEncryptor = new StandardPBEStringEncryptor();
        wrongEncryptor.setPassword(wrongKey);
        wrongEncryptor.setAlgorithm("PBEWithMD5AndDES");

        try {
            String decryptedText = wrongEncryptor.decrypt(encryptedText);
        }catch (EncryptionOperationNotPossibleException e) {
            System.out.println("복호화 실패");
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("3. Encrypt Util Test")
    void test3() {
        String targetText = "This is a secret message";
        String encrypt = JasyptEncryptUtil.encrypt(targetText);
        System.out.println("encrypt = " + encrypt);

        String decrypt = JasyptEncryptUtil.decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
        Assertions.assertThat(decrypt).isEqualTo(targetText);
    }
}
