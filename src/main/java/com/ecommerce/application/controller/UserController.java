package com.ecommerce.application.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.AuthResponseDto;
import com.ecommerce.application.entity.dtos.UserLoginDto;
import com.ecommerce.application.entity.dtos.UserRegisterDto;
import com.ecommerce.application.entity.dtos.UserResponseDto;
import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.service.UserService;
import com.ecommerce.application.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * User Controller
 * Handles user registration, login, and profile endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        log.info("Register request for email: {}", userRegisterDto.getEmail());
        return userService.registerUser(userRegisterDto);
    }

    /**
     * Login user and return JWT token
     */
    @PostMapping("/login")
    public AuthResponseDto loginUser(@RequestBody UserLoginDto userLoginDto) {
        log.info("Login request for email: {}", userLoginDto.getEmail());
        return userService.loginUser(userLoginDto);
    }

    /**
     * Get user profile (protected endpoint)
     */
    @GetMapping("/profile")
    public UserResponseDto getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        
        User user = userService.getUserByEmail(email);
        log.info("Profile request for user: {}", email);
        
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Get user by ID (protected endpoint)
     * Authorization: User can view own profile, admin can view any user
     */
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        
        // Get current user details from JWT
        User currentUser = userService.getUserByEmail(email);
        
        log.info("Get user request for id: {} by user: {}", id, email);
        return userService.getUserById(currentUser.getId(), currentUser.getRole(), id);
    }
}

