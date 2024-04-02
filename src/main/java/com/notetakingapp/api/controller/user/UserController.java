package com.notetakingapp.api.controller.user;

import com.notetakingapp.api.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getUsers() {
        return new ResponseEntity<>("Hello there", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable("id") String id) {
        return new ResponseEntity<>("User with id " + id, HttpStatus.OK);
    }

}
