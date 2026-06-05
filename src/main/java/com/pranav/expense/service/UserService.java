package com.pranav.expense.service;

import com.pranav.expense.dto.ExpenseDTO.*;
import com.pranav.expense.model.User;
import com.pranav.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service marks this as a Spring-managed bean
// Business logic lives here - controller should NOT talk to repository directly
@Service
public class UserService {

    // @Autowired = Spring automatically injects (creates and provides) the UserRepository object
    // You don't write: UserRepository repo = new UserRepository() - Spring does it
    @Autowired
    private UserRepository userRepository;

    public ApiResponse<UserResponse> register(RegisterRequest request) {
        // Check duplicate email - replaces your SQLIntegrityConstraintViolationException catch
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In future: use BCrypt to hash this

        // .save() → Hibernate runs INSERT INTO users (...) VALUES (...)
        User saved = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setUserId(saved.getUserId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());

        return ApiResponse.ok("Registration successful", response);
    }

    public ApiResponse<UserResponse> login(LoginRequest request) {
        // Replaces your manual PreparedStatement + ResultSet login code
        return userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setUserId(user.getUserId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    return ApiResponse.ok("Login successful", response);
                })
                .orElse(ApiResponse.error("Invalid email or password"));
    }
}
