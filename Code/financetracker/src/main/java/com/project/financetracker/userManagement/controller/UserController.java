package com.project.financetracker.userManagement.controller;

import com.project.financetracker.userManagement.dto.LoginDto;
import com.project.financetracker.userManagement.dto.UserDto;
import com.project.financetracker.userManagement.entity.User;
import com.project.financetracker.userManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto) {
        if (userService.loginUser(loginDto)) {
            return ResponseEntity.ok().body("Welcome " + loginDto.getEmail());
        } else {
            return ResponseEntity.badRequest().body("Incorrect email or password");
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> userRegister(@RequestBody UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        if(registeredUser != null) {
            return ResponseEntity.ok().body("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password, User already exists");
        }
    }

    @GetMapping(path = "/profile")
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User userProfile = userService.findUser(userEmail);

        if (userProfile != null) {
            return ResponseEntity.ok().body(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/profile")
    public ResponseEntity<?> userUpdate(@RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(userDto);

        if(updatedUser != null) {
            return ResponseEntity.ok().body("User updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Incorrect info");
        }
    }

}
