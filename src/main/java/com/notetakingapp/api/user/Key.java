package com.notetakingapp.api.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_key")
@Data
public class Key {

    @Id
    @Column(length = 36, updatable = false, name = "user_key_id")
    private String userKeyId;
    @Column(name = "public_key")
    private String publicKey;
    @Column(name = "private_key")
    private String privateKey;
    @Column(name = "symmetric_key")
    private String symmetricKey;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @Column(name = "salt1_key")
    private String salt1Key;
    @Column(name = "salt2_key")
    private String salt2Key;

}
