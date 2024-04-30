package com.notetakingapp.api.user;

import com.notetakingapp.api.auth.RegisterRequest;
import com.notetakingapp.api.crypto.AES;
import com.notetakingapp.api.crypto.RSA;
import com.notetakingapp.api.key.Key;
import com.notetakingapp.api.key.KeyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KeyRepository keyRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User createUser(RegisterRequest userRequest) throws Exception {
        String password = passwordEncoder.encode(userRequest.getPassword());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(password);
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
        key.setUserId(user.getId());
        User u = userRepository.saveAndFlush(user);
        keyRepository.saveAndFlush(key);
        return u;
    }

    private HashMap<String, String> generateAllKeys(String password) throws Exception {
        HashMap<String, String> keys = new HashMap<>();

        byte[] hashedByte = MessageDigest.getInstance("SHA-512").digest(password.getBytes());
        String hashedPass = Base64.getEncoder().encodeToString(hashedByte);
        byte[] randomBytes = new SecureRandom().generateSeed(32);
        String salt = Base64.getEncoder().encodeToString(randomBytes);
        String symmetricKey = Base64.getEncoder().encodeToString(AES.generateKey(hashedPass, salt).getEncoded());

        String salt1Key = Base64.getEncoder().encodeToString(AES.generateKey().getEncoded());
        String salt2Key = Base64.getEncoder().encodeToString(AES.generateKey().getEncoded());
        keys.put("salt1_key", salt1Key);
        keys.put("salt2_key", salt2Key);

        KeyPair keyPair = RSA.generateKeyPair();
        String p_puk = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String p_prk = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        keys.put("symmetric_key", RSA.encrypt(symmetricKey, p_puk));

        String c1_prk = AES.encrypt(p_prk, salt2Key);
        String c2_prk = AES.encrypt(c1_prk, salt1Key);
        keys.put("public_key", p_puk);
        keys.put("private_key", c2_prk);

        return keys;
    }

    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public String deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        keyRepository.deleteByUserId(user.orElseThrow().getId());
        userRepository.deleteById(id);
        return "OK";
    }

    public String getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return "{\"id\": \"" + user.getId() + "\", \"fullName\": \"" + user.getFullName() + "\", \"email\": \"" + email + "\"}";
    }
}
