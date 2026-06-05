package com.pranav.expense.controller;

import com.pranav.expense.dto.ExpenseDTO.*;
import com.pranav.expense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
// Means: every method returns JSON automatically (Spring uses Jackson to convert)
@RestController
@RequestMapping("/api/users")  // All endpoints in this class start with /api/users
public class UserController {

    @Autowired
    private UserService userService;

    // POST http://localhost:8080/api/users/register
    // Body (JSON): { "name": "Pranav", "email": "p@gmail.com", "password": "1234" }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        ApiResponse<UserResponse> response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    // POST http://localhost:8080/api/users/login
    // Body (JSON): { "email": "p@gmail.com", "password": "1234" }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        ApiResponse<UserResponse> response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
