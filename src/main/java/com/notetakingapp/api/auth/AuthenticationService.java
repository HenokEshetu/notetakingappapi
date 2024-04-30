package com.notetakingapp.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notetakingapp.api.config.JwtService;
import com.notetakingapp.api.crypto.AES;
import com.notetakingapp.api.crypto.RSA;
import com.notetakingapp.api.key.Key;
import com.notetakingapp.api.key.KeyRepository;
import com.notetakingapp.api.token.Token;
import com.notetakingapp.api.token.TokenRepository;
import com.notetakingapp.api.token.TokenType;
import com.notetakingapp.api.user.User;
import com.notetakingapp.api.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KeyRepository keyRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .id(UUID.randomUUID().toString())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        HashMap<String, String> keys = null;
        try {
            keys = generateAllKeys(request.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert keys != null;
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

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        keyRepository.save(key);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
