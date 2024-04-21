package com.notetakingapp.api.Utils.security;

//import com.notetakingapp.api.user.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

//import java.io.InputStream;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//
//import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

//    private KeyStore keyStore;
//
//    public void init() throws Exception {
//        keyStore = KeyStore.getInstance("JKS");
//        InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
//        keyStore.load(resourceAsStream, "secret".toCharArray());
//    }
//
//    public String generateToken(Authentication authentication) throws Exception {
//        User principal = (User) authentication.getPrincipal();
//        return Jwts.builder()
//                .setSubject(principal.getUsername())
//                .signWith(getPrivateKey())
//                .compact();
//    }
//
//    private PrivateKey getPrivateKey() throws Exception {
//        return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
//    }
//
//    public boolean validateToken(String jwt) throws Exception {
////        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
//        return true;
//    }
//
//    private PublicKey getPublicKey() throws Exception {
//        return keyStore.getCertificate("springblog").getPublicKey();
//    }
//
//    public String getUsernameFromJwt(String token) throws Exception {
////        Claims claims = parser()
////                .setSigningKey(getPublicKey())
////                .parseClaimsJws(token)
////                .getBody();
////        return claims.getSubject();
//        return "";
//    }

}
