package com.notetakingapp.api.Utils.crypto;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSA extends Crypto {

    public static String encrypt(String plainText, String publicKey) throws Exception {
        PublicKey originalPublicKey = loadPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, originalPublicKey);
        byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        PrivateKey originalPrivateKey = loadPrivateKey(key);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, originalPrivateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return Base64.getEncoder().encodeToString(decryptedBytes);
    }

    public static PublicKey loadPublicKey(String publicKey) throws Exception {
        publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        byte[] data = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }


    public static PrivateKey loadPrivateKey(String privateKey) throws Exception {
        privateKey = privateKey.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        byte[] clear = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey _privateKey = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return _privateKey;
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

}
