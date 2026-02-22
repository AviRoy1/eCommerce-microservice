package com.ecom.demo.controller;

import com.ecom.demo.dto.UserRequest;
import com.ecom.demo.dto.UserResponse;
import com.ecom.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/users")
    public List<UserResponse> getAllUsers() {
        return userService.fetchAllUsers();
    }

    @PostMapping("/api/users")
    public String createUser(@RequestBody UserRequest user) {
         userService.createUser(user);
        return "User is added!!";
    }

    @GetMapping("/api/user/{id}")
    public UserResponse findUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

}
