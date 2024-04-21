package com.notetakingapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.userId = ?1")
    Optional<User> findByUserId(String userId);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String username);

}
