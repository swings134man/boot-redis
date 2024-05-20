package com.boot.redis.config.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * @info : AES256Util
 *
 */
public class AES256Util {

    private static String key;
    private static String trans;

    private static byte[] iv;
    private static Key keySpec;


    public static void init(String aesKey, String ivKey) throws Exception{
        try{
            key = aesKey;
            iv = ivKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            trans = "AES/CBC/PKCS5Padding";

            byte[] keyBytes = new byte[32];
            int len = 0;
            byte[] b = null;
            try {
                b = aesKey.getBytes(StandardCharsets.UTF_8);
                len = b.length;
                if (len > keyBytes.length) {
                    len = keyBytes.length;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            keySpec = new SecretKeySpec(keyBytes, "AES");
        }catch (Exception e){
            throw new Exception("AES256Util init error", e);
        }
    }


    /**
     * encrypt - 암호화
     * @param aesKey
     * @param ivKey
     * @param txt
     * @return
     * @throws Exception
     */
    private static String encrypt(String aesKey, String ivKey, String txt) throws Exception{

        if(txt == null || txt.isEmpty()) {return txt;}

        init(aesKey, ivKey);
        byte[] encrypted = null;

        try {
            Cipher c = Cipher.getInstance(trans);

            if(trans.equals("AES/ECB/PKCS5Padding")) {
                c.init(Cipher.ENCRYPT_MODE, keySpec);
            }else {
                c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            }

            encrypted = c.doFinal(txt.getBytes(StandardCharsets.UTF_8));

        }catch (Exception e){
            throw new Exception("AES256Util encrypt error", e);
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }


    /**
     * decrypt - 복호화
     * @param aesKey
     * @param ivKey
     * @param txt
     * @return
     * @throws Exception
     */
    private static String decrypt(String aesKey, String ivKey, String txt) throws Exception{

        if(txt == null || txt.isEmpty()) {return txt;}

        init(aesKey, ivKey);
        String decrypted = "";

        try{
            Cipher c = Cipher.getInstance(trans);
            if(trans.equals("AES/ECB/PKCS5Padding")) {
                c.init(Cipher.DECRYPT_MODE, keySpec);
            }else {
                c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            }

            byte[] byteStr = Base64.getDecoder().decode(txt);

            decrypted = new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new Exception("AES256Util decrypt error", e);
        }

        return decrypted;
    }

    public static String encryptSafe(String aseKey, String ivKey, String txt) {
        try {
            String encrypt = encrypt(aseKey, ivKey, txt);
            return encrypt;
        } catch (Exception e) {
            return null;
        }
    }

    public static String decryptSafe(String aseKey, String ivKey, String txt) {
        try {
            String decrypt = decrypt(aseKey, ivKey, txt);
            return decrypt;
        } catch (Exception e) {
            return null;
        }
    }
}
