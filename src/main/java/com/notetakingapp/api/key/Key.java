package com.notetakingapp.api.key;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_key")
@Data
public class Key {

    @Id
    @Column(length = 36, updatable = false, name = "user_key_id")
    private String userKeyId;
    @Column(name = "public_key", columnDefinition = "TEXT")
    private String publicKey;
    @Column(name = "private_key", columnDefinition = "TEXT")
    private String privateKey;
    @Column(name = "symmetric_key", columnDefinition = "TEXT")
    private String symmetricKey;
    @Column(length = 36, updatable = false, name = "user_id")
    private String userId;
    @Column(name = "salt1_key", columnDefinition = "TEXT")
    private String salt1Key;
    @Column(name = "salt2_key", columnDefinition = "TEXT")
    private String salt2Key;

}
