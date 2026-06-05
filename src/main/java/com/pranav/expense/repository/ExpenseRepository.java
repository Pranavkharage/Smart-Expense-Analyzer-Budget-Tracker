package com.pranav.expense.repository;

import com.pranav.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // Find all expenses for a user - Spring generates: SELECT * FROM expenses WHERE user_id = ? ORDER BY expense_date DESC
    List<Expense> findByUserUserIdOrderByExpenseDateDesc(Integer userId);

    // Monthly report grouped by category - this is your monthlyReport() method from JDBC, now in JPQL
    // JPQL uses Java class/field names, not SQL table/column names
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " +
           "WHERE e.user.userId = :userId " +
           "AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE) " +
           "AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE) " +
           "GROUP BY e.category")
    List<Object[]> getMonthlyReportByCategory(@Param("userId") Integer userId);

    // Total spent this month
    @Query("SELECT SUM(e.amount) FROM Expense e " +
           "WHERE e.user.userId = :userId " +
           "AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE) " +
           "AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE)")
    Double getTotalSpentThisMonth(@Param("userId") Integer userId);
}
