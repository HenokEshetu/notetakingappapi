package com.notetakingapp.api.user;

import com.notetakingapp.api.Utils.crypto.AES;
import com.notetakingapp.api.Utils.crypto.RSA;
//import com.notetakingapp.api.Utils.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeyRepository keyRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtProvider jwtProvider;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User createUser(UserRegisterForm userRequest) throws Exception {
        String password = passwordEncoder.encode(userRequest.getPassword());

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
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
        key.setUserId(user.getUserId());
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
        Optional<User> user = userRepository.findByUserId(id);
        keyRepository.deleteByUserId(user.orElseThrow().getUserId());
        userRepository.deleteById(id);
        return "OK";
    }

    public AuthResponse login(UserLoginForm loginForm) throws Exception {
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        return new AuthResponse(token, loginForm.getUsername());
        return null;
    }

}
