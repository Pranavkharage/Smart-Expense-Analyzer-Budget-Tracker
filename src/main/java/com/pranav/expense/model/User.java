package com.pranav.expense.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

// @Entity tells Hibernate: "Map this Java class to a database table"
// This replaces all your ResultSet rs.getInt("user_id"), rs.getString("name") code
@Entity
@Table(name = "users")
@Data               // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: generates no-arg constructor (required by JPA)
@AllArgsConstructor // Lombok: generates full constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT in MySQL
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // One user has many expenses - Hibernate manages this JOIN automatically
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Expense> expenses;

    // One user has one budget
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Budget budget;
}
