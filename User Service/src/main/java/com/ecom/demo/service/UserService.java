package com.ecom.demo.service;

import com.ecom.demo.dto.UserRequest;
import com.ecom.demo.dto.UserResponse;
import com.ecom.demo.entity.Users;
import com.ecom.demo.mapper.UserMapper;
import com.ecom.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToUserResponse).toList();
    }

    public void createUser(UserRequest userRequest) {
        Users user = userMapper.convertToUser(userRequest);
        userRepository.save(user);
    }

    public UserResponse getUserById(String id) {
        Optional<Users> user = userRepository.findById(id);
        return user.stream()
                .map(userMapper::convertToUserResponse).findAny().orElse(null);
    }



}
