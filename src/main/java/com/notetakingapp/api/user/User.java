package com.notetakingapp.api.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name = "_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(length = 36, updatable = false)
    private String id;
    @NonNull
    private String fullName;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String profileImageUrl;
    @NonNull
    private String key;

}
