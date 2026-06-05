# Smart Expense Analyzer & Budget Tracker
### Spring Boot REST API | Upgraded from JDBC

---

## Tech Stack
- **Java 17**
- **Spring Boot 3.2**
- **Spring Web** – REST API (@RestController, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping)
- **Spring Data JPA + Hibernate** – ORM, replaces all JDBC boilerplate
- **MySQL** – Database
- **Lombok** – Removes getter/setter boilerplate
- **Maven** – Dependency management via pom.xml

---

## Project Structure
```
src/main/java/com/pranav/expense/
├── SmartExpenseApplication.java   ← Entry point (@SpringBootApplication)
├── controller/
│   ├── UserController.java        ← /api/users endpoints
│   └── ExpenseController.java     ← /api/expenses endpoints
├── service/
│   ├── UserService.java           ← Business logic for auth
│   └── ExpenseService.java        ← Business logic for expenses
├── repository/
│   ├── UserRepository.java        ← JpaRepository for users
│   ├── ExpenseRepository.java     ← JpaRepository + custom @Query
│   └── BudgetRepository.java      ← JpaRepository for budget
├── model/
│   ├── User.java                  ← @Entity mapped to users table
│   ├── Expense.java               ← @Entity mapped to expenses table
│   └── Budget.java                ← @Entity mapped to budget table
└── dto/
    └── ExpenseDTO.java            ← Request/Response data classes
```

---

## Setup

1. Create MySQL database:
```sql
CREATE DATABASE expense_tracker;
```

2. Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

3. Run:
```bash
mvn spring-boot:run
```
App starts at `http://localhost:8080`

---

## API Endpoints (Test on Postman)

### User Auth
| Method | URL | Body |
|--------|-----|------|
| POST | /api/users/register | `{"name":"Pranav","email":"p@gmail.com","password":"1234"}` |
| POST | /api/users/login | `{"email":"p@gmail.com","password":"1234"}` |

### Expenses
| Method | URL | Body / Param |
|--------|-----|------|
| POST | /api/expenses/add | `{"userId":1,"amount":500,"category":"Food","description":"Lunch"}` |
| GET | /api/expenses/user/{userId} | – |
| GET | /api/expenses/report/monthly/{userId} | – |
| POST | /api/expenses/budget | `{"userId":1,"monthlyLimit":5000}` |
| DELETE | /api/expenses/delete/{expenseId} | – |

---

## What changed from JDBC version

| Old (JDBC) | New (Spring Boot) |
|---|---|
| `DriverManager.getConnection(...)` | Auto-configured by `application.properties` |
| `PreparedStatement`, `ResultSet` | `JpaRepository.save()`, `findById()`, `findAll()` |
| Manual SQL in Java strings | Method names (`findByEmail`) or `@Query` |
| `SQLIntegrityConstraintViolationException` | `existsByEmail()` check |
| Object mapping from ResultSet | `@Entity` + Hibernate ORM |
| `Class.forName("com.mysql.cj.jdbc.Driver")` | Auto-loaded by Spring |
