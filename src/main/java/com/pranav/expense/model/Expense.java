package com.pranav.expense.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer expenseId;

    // @ManyToOne = many expenses belong to one user
    // Hibernate generates the JOIN automatically - no manual SQL needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "category", nullable = false, length = 30)
    private String category;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;
}
