# User Authentication Implementation Guide

## Overview
This e-commerce application now includes JWT-based authentication with Spring Security. Users can register and login to receive authentication tokens.

## Endpoints

### 1. **User Registration**
- **URL**: `POST /api/users/register`
- **Request Body**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```
- **Response**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### 2. **User Login**
- **URL**: `POST /api/users/login`
- **Request Body**:
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```
- **Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### 3. **Get User by ID** (Protected)
- **URL**: `GET /api/users/{id}`
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### 4. **Get User Profile** (Protected)
- **URL**: `GET /api/users/profile`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: User profile information

## Key Features Implemented

### 1. **Password Encoding**
- Passwords are encrypted using BCrypt
- Never stored in plain text in the database

### 2. **JWT Token Generation**
- Tokens are generated upon successful login
- Token contains user email and ID
- Default expiration: 24 hours (configurable)

### 3. **JWT Authentication Filter**
- Automatically validates JWT tokens on protected endpoints
- Extracts user information from token
- Sets authentication context for the request

### 4. **Security Configuration**
- CSRF protection disabled for API
- Stateless session management
- Public endpoints: `/api/users/register`, `/api/users/login`, `/h2-console/**`
- Protected endpoints: All other endpoints require valid JWT token

## Configuration

Edit `application.properties` to customize:
```properties
# JWT Secret (change this to a secure value in production)
jwt.secret=your-super-secret-key-for-jwt-token-generation-make-it-long-and-secure-at-least-32-characters

# JWT Expiration in milliseconds (86400000 = 24 hours)
jwt.expiration=86400000
```

## How to Use

### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

### 2. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

### 3. Access Protected Endpoint with Token
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Files Created/Modified

### New Files:
1. `src/main/java/com/ecommerce/application/util/JwtUtil.java` - JWT token generation and validation
2. `src/main/java/com/ecommerce/application/filter/JwtAuthenticationFilter.java` - JWT authentication filter
3. `src/main/java/com/ecommerce/application/config/SecurityConfig.java` - Spring Security configuration
4. `src/main/java/com/ecommerce/application/entity/dtos/UserLoginDto.java` - Login request DTO
5. `src/main/java/com/ecommerce/application/entity/dtos/AuthResponseDto.java` - Authentication response DTO

### Modified Files:
1. `build.gradle` - Added Spring Security and JWT dependencies
2. `src/main/java/com/ecommerce/application/service/UserService.java` - Added password encoding and login logic
3. `src/main/java/com/ecommerce/application/controller/UserController.java` - Added login and profile endpoints
4. `src/main/java/com/ecommerce/application/repositary/UserRepositary.java` - Added findByEmail method
5. `src/main/resources/application.properties` - Added JWT and database configuration

## Security Best Practices

1. **Change JWT Secret**: Update the `jwt.secret` property with a long, random string
2. **Use HTTPS**: Always use HTTPS in production
3. **Token Rotation**: Implement token refresh mechanism for long-running sessions
4. **Rate Limiting**: Implement rate limiting on login endpoint
5. **Password Policy**: Enforce strong password requirements
6. **Input Validation**: Add validation for email format and password strength
