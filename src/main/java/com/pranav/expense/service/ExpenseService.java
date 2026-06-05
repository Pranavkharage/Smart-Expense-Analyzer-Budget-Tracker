package com.pranav.expense.service;

import com.pranav.expense.dto.ExpenseDTO.*;
import com.pranav.expense.model.Budget;
import com.pranav.expense.model.Expense;
import com.pranav.expense.model.User;
import com.pranav.expense.repository.BudgetRepository;
import com.pranav.expense.repository.ExpenseRepository;
import com.pranav.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    // Add expense - replaces your addExpense() JDBC method
    public ApiResponse<ExpenseResponse> addExpense(AddExpenseRequest request) {
        // Find user first
        return userRepository.findById(request.getUserId())
                .map(user -> {
                    Expense expense = new Expense();
                    expense.setUser(user);  // Hibernate handles the user_id FK automatically
                    expense.setAmount(request.getAmount());
                    expense.setCategory(request.getCategory());
                    expense.setDescription(request.getDescription());
                    // Use today if no date provided
                    expense.setExpenseDate(
                        request.getExpenseDate() != null ? request.getExpenseDate() : LocalDate.now()
                    );

                    // Hibernate runs: INSERT INTO expenses (user_id, amount, ...) VALUES (?, ?, ...)
                    Expense saved = expenseRepository.save(expense);

                    ExpenseResponse response = mapToResponse(saved);
                    return ApiResponse.ok("Expense added successfully", response);
                })
                .orElse(ApiResponse.error("User not found"));
    }

    // View all expenses for a user - replaces your viewExpenses() JDBC method
    public ApiResponse<List<ExpenseResponse>> getExpensesByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            return ApiResponse.error("User not found");
        }

        // Hibernate runs: SELECT * FROM expenses WHERE user_id = ? ORDER BY expense_date DESC
        List<Expense> expenses = expenseRepository.findByUserUserIdOrderByExpenseDateDesc(userId);
        List<ExpenseResponse> responses = expenses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ApiResponse.ok("Expenses fetched", responses);
    }

    // Monthly report - replaces your monthlyReport() JDBC method
    public ApiResponse<MonthlyReportResponse> getMonthlyReport(Integer userId) {
        if (!userRepository.existsById(userId)) {
            return ApiResponse.error("User not found");
        }

        // Get category-wise breakdown using @Query in repository
        List<Object[]> rawData = expenseRepository.getMonthlyReportByCategory(userId);
        final double grandTotal = expenseRepository.getTotalSpentThisMonth(userId) != null ? expenseRepository.getTotalSpentThisMonth(userId) : 0.0;

        List<MonthlyReportResponse.CategoryTotal> breakdown = new ArrayList<>();
        for (Object[] row : rawData) {
            MonthlyReportResponse.CategoryTotal ct = new MonthlyReportResponse.CategoryTotal();
            ct.setCategory((String) row[0]);
            ct.setTotal(((Number) row[1]).doubleValue());
            breakdown.add(ct);
        }

        MonthlyReportResponse report = new MonthlyReportResponse();
        report.setBreakdown(breakdown);
        report.setGrandTotal(grandTotal);

        // Check against budget
        budgetRepository.findByUserId(userId).ifPresent(budget -> {
            report.setBudgetLimit(budget.getMonthlyLimit());
            report.setBudgetStatus(
                grandTotal > budget.getMonthlyLimit() ? "EXCEEDED ⚠️" : "WITHIN LIMIT ✅"
            );
        });

        return ApiResponse.ok("Monthly report generated", report);
    }

    // Set or update budget limit
    public ApiResponse<String> setBudget(SetBudgetRequest request) {
        return userRepository.findById(request.getUserId())
                .map(user -> {
                    Budget budget = budgetRepository.findByUserId(request.getUserId())
                            .orElse(new Budget());
                    budget.setUser(user);
                    budget.setMonthlyLimit(request.getMonthlyLimit());
                    budgetRepository.save(budget);
                    return ApiResponse.ok("Budget set to ₹" + request.getMonthlyLimit(), (String) null);
                })
                .orElse(ApiResponse.error("User not found"));
    }

    // Update expense
    public ApiResponse<ExpenseResponse> updateExpense(Integer expenseId, AddExpenseRequest request) {
        return expenseRepository.findById(expenseId)
                .map(expense -> {
                    if (request.getAmount() != null)
                        expense.setAmount(request.getAmount());
                    if (request.getCategory() != null)
                        expense.setCategory(request.getCategory());
                    if (request.getDescription() != null)
                        expense.setDescription(request.getDescription());
                    if (request.getExpenseDate() != null)
                        expense.setExpenseDate(request.getExpenseDate());
                    Expense saved = expenseRepository.save(expense);
                    return ApiResponse.ok("Expense updated successfully", mapToResponse(saved));
                })
                .orElse(ApiResponse.error("Expense not found"));
    }

    // Update budget
    public ApiResponse<String> updateBudget(Integer userId, SetBudgetRequest request) {
        return budgetRepository.findByUserId(userId)
                .map(budget -> {
                    budget.setMonthlyLimit(request.getMonthlyLimit());
                    budgetRepository.save(budget);
                    return ApiResponse.ok("Budget updated to Rs." + request.getMonthlyLimit(), (String) null);
                })
                .orElse(ApiResponse.error("No budget found. Use POST /api/expenses/budget to create one first."));
    }

    // Delete expense
    public ApiResponse<String> deleteExpense(Integer expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            return ApiResponse.error("Expense not found");
        }
        // Hibernate runs: DELETE FROM expenses WHERE expense_id = ?
        expenseRepository.deleteById(expenseId);
        return ApiResponse.ok("Expense deleted", null);
    }

    // Helper: map Expense entity to ExpenseResponse DTO
    private ExpenseResponse mapToResponse(Expense e) {
        ExpenseResponse r = new ExpenseResponse();
        r.setExpenseId(e.getExpenseId());
        r.setAmount(e.getAmount());
        r.setCategory(e.getCategory());
        r.setDescription(e.getDescription());
        r.setExpenseDate(e.getExpenseDate());
        return r;
    }
}
