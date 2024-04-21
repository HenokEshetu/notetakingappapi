package com.notetakingapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KeyRepository extends JpaRepository<Key, String> {

    @Query("SELECT k FROM Key k WHERE k.userId = ?1")
    Optional<Key> findKeyByUserId(String userId);

    void deleteByUserId(String userId);

}
