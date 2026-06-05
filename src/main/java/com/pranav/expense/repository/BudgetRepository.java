package com.pranav.expense.repository;

import com.pranav.expense.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    // SELECT * FROM budget WHERE user_id = ?
    Optional<Budget> findByUserId(Integer userId);
}
