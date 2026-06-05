package com.pranav.expense.controller;

import com.pranav.expense.dto.ExpenseDTO.*;
import com.pranav.expense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // POST http://localhost:8080/api/expenses/add
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(@RequestBody AddExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    // GET http://localhost:8080/api/expenses/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getExpenses(@PathVariable Integer userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUser(userId));
    }

    // GET http://localhost:8080/api/expenses/report/monthly/1
    @GetMapping("/report/monthly/{userId}")
    public ResponseEntity<ApiResponse<MonthlyReportResponse>> getMonthlyReport(@PathVariable Integer userId) {
        return ResponseEntity.ok(expenseService.getMonthlyReport(userId));
    }

    // POST http://localhost:8080/api/expenses/budget
    @PostMapping("/budget")
    public ResponseEntity<ApiResponse<String>> setBudget(@RequestBody SetBudgetRequest request) {
        return ResponseEntity.ok(expenseService.setBudget(request));
    }

    // PUT http://localhost:8080/api/expenses/update/1
    @PutMapping("/update/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Integer expenseId,
            @RequestBody AddExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseId, request));
    }

    // PUT http://localhost:8080/api/expenses/budget/update/1
    @PutMapping("/budget/update/{userId}")
    public ResponseEntity<ApiResponse<String>> updateBudget(
            @PathVariable Integer userId,
            @RequestBody SetBudgetRequest request) {
        return ResponseEntity.ok(expenseService.updateBudget(userId, request));
    }

    // DELETE http://localhost:8080/api/expenses/delete/5
    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable Integer expenseId) {
        return ResponseEntity.ok(expenseService.deleteExpense(expenseId));
    }
}