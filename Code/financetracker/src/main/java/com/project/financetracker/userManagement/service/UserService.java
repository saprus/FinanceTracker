package com.project.financetracker.userManagement.service;

import com.project.financetracker.userManagement.dto.LoginDto;
import com.project.financetracker.userManagement.dto.UserDto;
import com.project.financetracker.userManagement.entity.User;
import com.project.financetracker.userManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserDto userDto) {

        // Check if user exists
        User userOptionalEmail = userRepository.findUserByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email is already in use"));

        //Encode Password by using passwordEncoder.BCryptPasswordEncoder()
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        //Save User
        User newUser = new User(userDto.getName(), userDto.getEmail(), encodedPassword);
        return userRepository.save(newUser);
    }

    public boolean loginUser(LoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
    }

    public User updateUser(UserDto userDto) {
        // Check if user exists
        User userOptional = userRepository.findUserByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(userDto.getName() != null && !userDto.getName().isEmpty() && !Objects.equals(userDto.getName(), userOptional.getName())) {
            userOptional.setName(userDto.getName());
        }
        return userRepository.save(userOptional);
    }

    public User findUser(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
