package com.ecommerce.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.application.entity.User;
import com.ecommerce.application.entity.dtos.AuthResponseDto;
import com.ecommerce.application.entity.dtos.UserLoginDto;
import com.ecommerce.application.entity.dtos.UserRegisterDto;
import com.ecommerce.application.entity.dtos.UserResponseDto;
import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.exception.BadRequestException;
import com.ecommerce.application.exception.ResourceNotFoundException;
import com.ecommerce.application.exception.UnauthorizedException;
import com.ecommerce.application.repositary.UserRepositary;
import com.ecommerce.application.util.AuthorizationUtil;
import com.ecommerce.application.util.JwtUtil;
import com.ecommerce.application.util.ValidationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * User Service with business logic and service-level authorization
 * Follows SOLID principles: Single Responsibility, DIP
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositary userRepositary;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final AuthorizationUtil authorizationUtil;

    /**
     * Register a new user with CUSTOMER role
     * @param userRegisterDto User registration data
     * @return UserResponseDto User details
     */
    public UserResponseDto registerUser(UserRegisterDto userRegisterDto) {
        log.info("Registering new user with email: {}", userRegisterDto.getEmail());

        // Validate input
        validationUtil.validateNotEmpty(userRegisterDto.getName(), "name");
        validationUtil.validateEmail(userRegisterDto.getEmail());
        validationUtil.validatePassword(userRegisterDto.getPassword());

        // Check if user with the same email already exists
        if (userRepositary.existsByEmail(userRegisterDto.getEmail())) {
            log.warn("Registration failed: Email already exists {}", userRegisterDto.getEmail());
            throw new BadRequestException("email", "Email already registered");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());

        // Create user with CUSTOMER role
        User user = User.builder()
                .name(userRegisterDto.getName())
                .email(userRegisterDto.getEmail())
                .password(encodedPassword)
                .role(Role.CUSTOMER)  // Default role
                .build();

        // Save user to the database
        User savedUser = userRepositary.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        return mapToUserResponseDto(savedUser);
    }

    /**
     * Authenticate user and generate JWT token
     * @param userLoginDto Login credentials
     * @return AuthResponseDto Token and user details
     */
    public AuthResponseDto loginUser(UserLoginDto userLoginDto) {
        log.info("User login attempt for email: {}", userLoginDto.getEmail());

        // Find user by email
        User user = userRepositary.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found with email {}", userLoginDto.getEmail());
                    return new UnauthorizedException();
                });

        // Verify password
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for email {}", userLoginDto.getEmail());
            throw new UnauthorizedException();
        }

        // Generate JWT token with role
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Get user by ID (service-level authorization check)
     * @param userId Current user ID
     * @param userRole Current user role
     * @param targetUserId User ID to retrieve
     * @return UserResponseDto User details
     */
    public UserResponseDto getUserById(Long userId, Role userRole, Long targetUserId) {
        log.info("Fetching user details for id: {}", targetUserId);

        // Authorization: Can only view own profile or admin can view any
        authorizationUtil.checkUserOrAdmin(userRole, userId, targetUserId);

        User user = userRepositary.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", targetUserId));

        return mapToUserResponseDto(user);
    }

    /**
     * Get user by email (internal use)
     */
    public User getUserByEmail(String email) {
        return userRepositary.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Get user by ID (internal use)
     */
    public User getUserEntityById(Long id) {
        return userRepositary.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    /**
     * Map User entity to UserResponseDto
     */
    private UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}