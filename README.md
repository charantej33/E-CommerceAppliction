# E-Commerce Application

A comprehensive Spring Boot-based RESTful API for an e-commerce platform with user authentication, product management, order processing, and category management.

## Features

- **User Authentication & Authorization**
  - User registration and login with JWT token-based authentication
  - Role-based access control (ADMIN and USER roles)
  - Secure password storage and validation
  - JWT token generation and validation

- **Product Management**
  - Create, read, update, and delete products
  - Product categorization
  - Product inventory management
  - Admin-only product management endpoints

- **Category Management**
  - Create and manage product categories
  - Category-based product organization
  - Admin-only category management

- **Order Processing**
  - Create and manage customer orders
  - Order status tracking (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
  - Order item management
  - User-specific order history

- **Security**
  - Spring Security integration
  - JWT-based stateless authentication
  - Authorization filters for endpoint protection
  - Exception handling with custom error responses

- **Database**
  - H2 in-memory database for development/testing
  - Support for SQL Server for production
  - Hibernate JPA for ORM

## Tech Stack

- **Backend Framework**: Spring Boot 4.0.1
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: H2 (development), SQL Server (production-ready)
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **ORM**: Spring Data JPA, Hibernate
- **Dependency Injection**: Spring Dependency Injection
- **Logging**: SLF4J
- **Code Generation**: Lombok

## Project Structure

```
src/main/java/com/ecommerce/application/
├── config/               # Spring Boot configuration classes
│   ├── GlobalExceptionHandler.java
│   └── SecurityConfig.java
├── controller/           # REST API endpoints
│   ├── UserController.java
│   ├── ProductController.java
│   ├── CategoryController.java
│   └── OrderController.java
├── entity/              # JPA entities and DTOs
│   ├── User.java
│   ├── Product.java
│   ├── Category.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── dtos/            # Data Transfer Objects
│   └── enums/           # Enumeration classes
├── service/             # Business logic layer
│   ├── UserService.java
│   ├── ProductService.java
│   ├── CategoryService.java
│   └── OrderService.java
├── repositary/          # Data access layer
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── CategoryRepository.java
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
├── exception/           # Custom exception classes
├── filter/             # Security filters
│   └── JwtAuthenticationFilter.java
├── util/               # Utility classes
│   ├── JwtUtil.java
│   ├── ValidationUtil.java
│   └── AuthorizationUtil.java
└── EcommerceapplicationApplication.java  # Main application class
```

## Prerequisites

- Java 21 or higher
- Gradle 7.x or higher
- Git

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/charantej33/E-CommerceAppliction.git
cd E-CommerceAppliction
```

### 2. Build the Project

```bash
# Using Gradle
gradle build

# Or using Gradle wrapper
./gradlew build
```

### 3. Configure the Application

Edit `src/main/resources/application.properties` to customize your settings:

```properties
# Server Configuration
server.port=8080

# Database Configuration (H2 - Development)
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# JWT Configuration
jwt.secret=your-super-secret-key-for-jwt-token-generation-make-it-long-and-secure
jwt.expiration=86400000
```

## Running the Application

### Using Gradle

```bash
# Development mode
gradle bootRun

# Or
./gradlew bootRun
```

### Using Java

```bash
gradle build
java -jar build/libs/ecommerceapplication-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### User Endpoints
- `POST /api/users/register` - Register a new user
- `POST /api/users/login` - User login (returns JWT token)
- `GET /api/users/{id}` - Get user profile (Authenticated)
- `PUT /api/users/{id}` - Update user profile (Authenticated)

### Product Endpoints
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `DELETE /api/products/{id}` - Delete product (ADMIN only)

### Category Endpoints
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category (ADMIN only)
- `PUT /api/categories/{id}` - Update category (ADMIN only)
- `DELETE /api/categories/{id}` - Delete category (ADMIN only)

### Order Endpoints
- `POST /api/orders` - Create new order (Authenticated)
- `GET /api/orders` - Get user's orders (Authenticated)
- `GET /api/orders/{id}` - Get order details (Authenticated)
- `PUT /api/orders/{id}` - Update order status (ADMIN only)
- `DELETE /api/orders/{id}` - Cancel order (Authenticated)

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. To access protected endpoints:

1. Register a new user or login with existing credentials
2. Include the JWT token in the `Authorization` header of subsequent requests:
   ```
   Authorization: Bearer <your_jwt_token>
   ```

## Database Access

The H2 console is available at:
```
http://localhost:8080/h2-console
```

**Default credentials:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (blank)

## Development

### Building

```bash
./gradlew build
```

### Running Tests

```bash
./gradlew test
```

### Code Quality

The project follows Spring Boot best practices with:
- Proper exception handling
- Input validation
- Security-first approach
- RESTful API design
- Separation of concerns (Controllers → Services → Repositories)

## Roles & Permissions

### ADMIN Role
- Create, update, delete products
- Create, update, delete categories
- View and manage all orders
- Modify order status

### USER Role
- View products and categories
- Create and manage own orders
- View own order history

## Error Handling

The application provides comprehensive error responses with appropriate HTTP status codes:

- `400 Bad Request` - Invalid input or validation errors
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server-side errors

All error responses include a detailed error message and timestamp for debugging.

## Future Enhancements

- [ ] Payment integration (Stripe, PayPal)
- [ ] Email notifications
- [ ] Product reviews and ratings
- [ ] Cart management
- [ ] Wishlist functionality
- [ ] Advanced search and filtering
- [ ] Order tracking with real-time updates
- [ ] Admin dashboard
- [ ] API documentation (Swagger/OpenAPI)

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is open source and available under the MIT License.

## Support

For questions or support, please open an issue in the [GitHub repository](https://github.com/charantej33/E-CommerceAppliction).

## Author

**Charantej**
- GitHub: [@charantej33](https://github.com/charantej33)

---

**Last Updated**: January 21, 2026
**Version**: 0.0.1-SNAPSHOT
