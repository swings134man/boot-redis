package com.boot.redis.util;

import com.boot.redis.config.util.JasyptEncryptUtil;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class EncryptTest {

    @Test
    @DisplayName("1. Encrypt Test")
    void use_1() {
        String encryptKey = "encryptKeyTestKey";
        String targetStringKey = "Test 1234";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptKey);

        String encryptedText = encryptor.encrypt(targetStringKey);
        System.out.println("encryptedText = " + encryptedText);
        Assertions.assertThat(encryptedText).isNotEmpty();

        String decryptedText = encryptor.decrypt(encryptedText);
        Assertions.assertThat(decryptedText).isEqualTo(targetStringKey);
        System.out.println("decryptedText = " + decryptedText);
    }

    // -----------

    @Test
    @DisplayName("2. Encrypt Util Test")
    void use_2() {
        String targetString = "Test 1234";

        String encrypt = JasyptEncryptUtil.encrypt(targetString);
        System.out.println("encrypt = " + encrypt);

        String decrypt = JasyptEncryptUtil.decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
        Assertions.assertThat(decrypt).isEqualTo(targetString);
    }

    @Test
    @DisplayName("3. Encrypt Util Test - Map Type")
    void use_3() {
        Map<String, Object> map = Map.of("id", "this is id", "pw", "this is pw");

        String encrypt = JasyptEncryptUtil.mapToStringEncrypt(map);
        System.out.println("encrypt = " + encrypt);

        Map<String, Object> decryptRes = JasyptEncryptUtil.mapToStringDecrypt(encrypt);
        System.out.println("decryptRes = " + decryptRes.toString());

        Assertions.assertThat(decryptRes).isEqualTo(map);
        if(decryptRes instanceof Map) System.out.println("decryptRes is Map Type");

    }

    @Test
    @DisplayName("4. DB Pw Encrypt")
    void use_4() {
        String targetString = "";

        String encrypt = JasyptEncryptUtil.encrypt(targetString);
        System.out.println("encrypt = " + encrypt);

        String decrypt = JasyptEncryptUtil.decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
        Assertions.assertThat(decrypt).isEqualTo(targetString);
    }

    @Test
    @DisplayName("5. decrypt Test")
    void use_5() {
        String encryptKey = "ejoa6pqPy+JT2q9e5HPK9DwgSYcA5gE55CKqPadfPbEr5YHrrvXNfF6jurcT1MaY";

        String decrypt = JasyptEncryptUtil.decrypt(encryptKey);
        System.out.println("decrypt = " + decrypt);
    }

}
