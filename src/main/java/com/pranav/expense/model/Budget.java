package com.pranav.expense.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "budget")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "monthly_limit", nullable = false)
    private Double monthlyLimit;

    // One budget belongs to one user
    @OneToOne
    @MapsId  // uses user_id from User as this entity's primary key
    @JoinColumn(name = "user_id")
    private User user;
}
