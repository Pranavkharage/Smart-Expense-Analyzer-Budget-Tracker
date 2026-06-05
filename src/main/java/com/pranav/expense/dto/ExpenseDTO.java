package com.pranav.expense.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// DTO = Data Transfer Object
// We use these instead of sending the full @Entity objects in API responses
// Reason: Entity has password field, relationships etc. - we don't want to expose all that

public class ExpenseDTO {

    // --- Request DTOs (what Postman/client sends) ---

    @Data
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class AddExpenseRequest {
        private Integer userId;
        private Double amount;
        private String category;
        private String description;
        // If not provided, defaults to today in the service layer
        private LocalDate expenseDate;
    }

    @Data
    public static class SetBudgetRequest {
        private Integer userId;
        private Double monthlyLimit;
    }

    // --- Response DTOs (what API sends back) ---

    @Data
    public static class UserResponse {
        private Integer userId;
        private String name;
        private String email;
        // NOTE: no password field here - never return passwords in responses
    }

    @Data
    public static class ExpenseResponse {
        private Integer expenseId;
        private Double amount;
        private String category;
        private String description;
        private LocalDate expenseDate;
    }

    @Data
    public static class MonthlyReportResponse {
        private List<CategoryTotal> breakdown;
        private Double grandTotal;
        private Double budgetLimit;
        private String budgetStatus; // "WITHIN LIMIT" or "EXCEEDED"

        @Data
        public static class CategoryTotal {
            private String category;
            private Double total;
        }
    }

    @Data
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> ok(String message, T data) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = true;
            r.message = message;
            r.data = data;
            return r;
        }

        public static <T> ApiResponse<T> error(String message) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = false;
            r.message = message;
            return r;
        }
    }
}
