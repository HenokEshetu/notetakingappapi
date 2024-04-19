package com.notetakingapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeyRepository extends JpaRepository<Key, String> {

    Optional<String> findKeyByUserId(String userId);

}
