package com.boot.redis.business.board.logic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @info : AesEncryptTest
 * @name : AesEncryptTest
 * @date : 2024/05/17 5:12 PM
 * @version : 1.0.0
 * @Description :
 *
 */
@SpringBootTest
class AesEncryptTest {

    final String trans = "AES/CBC/PKCS5Padding";
    final String aesKey = "34044627275ea068ee2b3fb642d82d55f609eb04eae6141da282cf7fcf7d58d4";
    final String ivKey = "3cd65cab914658a746bee2d4d7e2cc4a5d3303f0c6d0e61e14c49c4b1d423f85";
    final byte[] iv = ivKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);
    final String testParam = "This is Target Of Encrypt";

    Key keySpec;
    String encryptedText = "";

    @DisplayName("1. KeySpec 정의")
    @BeforeEach
    @Test
    void keyGen() {
        // keySpec 정의
        // need: aesKey
        byte[] keyBytes = new byte[32];
        int len = 0;
        byte[] b = null;
        try {
            b = aesKey.getBytes("UTF-8");
            len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");
        System.out.println("keySpec = " + keySpec);
    }


    @DisplayName("최종 암복호화 테스트")
    @Test
    void 암복호화_test() {
        // 암호화
        // need: aesKey, ivKey, Text
        byte[] encrypt = null;

        try {
            Cipher c = Cipher.getInstance(trans);

            if(trans.equals("AES/ECB/PKCS5Padding")) {
                c.init(Cipher.ENCRYPT_MODE, keySpec);
            } else {
                c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            }

            encrypt = c.doFinal(testParam.getBytes(StandardCharsets.UTF_8));
            encryptedText = new String(Base64.getEncoder().encodeToString(encrypt));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("encryptedText(암호화) = " + encryptedText);


        // 복호화
        String decrypt = "";
        try {
            Cipher c = Cipher.getInstance(trans);
            if(trans.equals("AES/ECB/PKCS5Padding")) {
                c.init(Cipher.DECRYPT_MODE, keySpec);
            } else {
                c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            }

            byte[] byteStr = Base64.getDecoder().decode(encryptedText);

            decrypt = new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("decrypt(복호화) = " + decrypt);

        Assertions.assertThat(testParam).isEqualTo(decrypt);
    }// method
}