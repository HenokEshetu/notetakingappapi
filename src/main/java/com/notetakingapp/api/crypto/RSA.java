package com.notetakingapp.api.crypto;

import javax.crypto.Cipher;
import java.security.*;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    public static String encrypt(String plainText, String publicKey) throws Exception {
        PublicKey originalPublicKey = loadPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, originalPublicKey);
        byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        PrivateKey originalPrivateKey = loadPrivateKey(key);
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, originalPrivateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }

    public static PublicKey loadPublicKey(String publicKey) throws Exception {
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        return fact.generatePublic(spec);
    }

    public static PrivateKey loadPrivateKey(String privateKey) throws Exception {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        return fact.generatePrivate(keySpec);
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

}
