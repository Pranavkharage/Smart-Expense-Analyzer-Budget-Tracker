package com.pranav.expense.repository;

import com.pranav.expense.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// JpaRepository<User, Integer> gives you these methods FREE - no SQL needed:
// save(user)         → INSERT or UPDATE
// findById(id)       → SELECT WHERE user_id = ?
// findAll()          → SELECT * FROM users
// deleteById(id)     → DELETE WHERE user_id = ?
// existsById(id)     → SELECT COUNT(*)

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Spring Data JPA reads the method name and generates SQL automatically!
    // findByEmail → SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT * FROM users WHERE email = ? AND password = ?
    Optional<User> findByEmailAndPassword(String email, String password);

    // SELECT COUNT(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);
}
