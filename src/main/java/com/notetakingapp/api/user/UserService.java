package com.notetakingapp.api.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "OK";
    }

}
