package com.notetakingapp.api.user;

import com.notetakingapp.api.Utils.crypto.AES;
import com.notetakingapp.api.Utils.crypto.Crypto;
import com.notetakingapp.api.Utils.crypto.RSA;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User createUser(UserRegisterForm userRequest) throws Exception {
        String password = passwordEncoder.encode(userRequest.getPassword());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(password);
        user.setProfileImageUrl(userRequest.getProfileImageUrl());
        user.setRole(Role.USER);

        HashMap<String, String> keys = generateAllKeys(password);
        String symmetricKey = keys.get("symmetric_key");
        String salt1Key = keys.get("salt1_key");
        String salt2Key = keys.get("salt2_key");
        String publicKey = keys.get("public_key");
        String privateKey = keys.get("private_key");

        Key key = new Key();
        key.setUserKeyId(UUID.randomUUID().toString());
        key.setPublicKey(publicKey);
        key.setPrivateKey(privateKey);
        key.setSalt1Key(salt1Key);
        key.setSalt2Key(salt2Key);
        key.setSymmetricKey(symmetricKey);
        key.setUser(user);

        return userRepository.save(user);
    }

    private HashMap<String, String> generateAllKeys(String password) throws Exception {
        HashMap<String, String> keys = new HashMap<>();

        byte[] hashedByte = MessageDigest.getInstance("SHA-512").digest(password.getBytes());
        String hashedPass = Crypto.bytesToHex(hashedByte);
        byte[] randomBytes = new SecureRandom().generateSeed(32);
        String salt = Base64.getEncoder().encodeToString(randomBytes);
        String symmetricKey = Crypto.bytesToHex(AES.generateKey(hashedPass, salt).getEncoded());

        String salt1Key = Crypto.bytesToHex(AES.generateKey().getEncoded());
        String salt2Key = Crypto.bytesToHex(AES.generateKey().getEncoded());
        keys.put("salt1_key", salt1Key);
        keys.put("salt2_key", salt2Key);

        KeyPair keyPair = RSA.generateKeyPair();
        byte[] p_puk = keyPair.getPublic().getEncoded();
        byte[] p_prk = keyPair.getPrivate().getEncoded();

        keys.put("symmetric_key", RSA.encrypt(symmetricKey, Crypto.bytesToHex(p_puk)));

        byte[] c1_puk = AES.encrypt(p_puk, salt1Key.getBytes());
        byte[] c1_prk = AES.encrypt(p_prk, salt2Key.getBytes());
        byte[] c2_puk = AES.decrypt(c1_puk, salt2Key.getBytes());
        byte[] c2_prk = AES.decrypt(c1_prk, salt1Key.getBytes());
        String publicKey = Crypto.bytesToHex(c2_puk);
        String privateKey = Crypto.bytesToHex(c2_prk);
        keys.put("public_key", publicKey);
        keys.put("private_key", privateKey);

        return keys;
    }

    private HashMap<String, String> decryptAllKeys() throws Exception {

    }

    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "OK";
    }

}
