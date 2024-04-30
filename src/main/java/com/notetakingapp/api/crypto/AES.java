package com.notetakingapp.api.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AES {

    public static String encrypt(String plainText, String key) throws Exception {
        byte[] encodedKey = Base64.getDecoder().decode(key);
        return Base64.getEncoder().encodeToString(encrypt(plainText.getBytes(), encodedKey));
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        byte[] encodedKey = Base64.getDecoder().decode(key);
        return new String(decrypt(cipherText.getBytes(), encodedKey));
    }

    public static byte[] encrypt(byte[] plainText, byte[] key) throws Exception {
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        return cipher.doFinal(plainText);
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key) throws Exception {
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        return cipher.doFinal(Base64.getDecoder().decode(cipherText));
    }

    public static SecretKey generateKey(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    public static SecretKey generateKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }

}
