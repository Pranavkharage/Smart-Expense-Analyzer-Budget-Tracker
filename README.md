# Smart Expense Analyzer & Budget Tracker
### A Full CRUD RESTful Backend API built with Spring Boot, Spring Data JPA, and MySQL

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apachemaven)
![Postman](https://img.shields.io/badge/Tested%20on-Postman-orange?style=flat-square&logo=postman)

---

## About the Project

**Smart Expense Analyzer & Budget Tracker** is a backend REST API that helps users track daily expenses, set monthly budget limits, and generate category-wise spending reports.

### Key Highlights
- Per-user data isolation — every user sees only their own expenses
- Monthly category-wise reports with budget threshold alerts
- Full CRUD — Create, Read, Update, Delete on both expenses and budget
- Layered architecture — Controller → Service → Repository → Database
- Hibernate ORM — Java objects mapped to DB tables automatically
- All endpoints tested on Postman

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2.0 |
| Web Layer | Spring Web (REST API) |
| ORM / Database Layer | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 |
| Build Tool | Maven (pom.xml) |
| API Testing | Postman |

---

## Project Structure

```
smart-expense/
├── pom.xml
└── src/main/
    ├── java/com/pranav/expense/
    │   ├── SmartExpenseApplication.java    ← Entry point (@SpringBootApplication)
    │   ├── controller/
    │   │   ├── UserController.java         ← /api/users  (register, login)
    │   │   └── ExpenseController.java      ← /api/expenses (full CRUD + reports)
    │   ├── service/
    │   │   ├── UserService.java            ← Auth business logic
    │   │   └── ExpenseService.java         ← Expense + budget logic
    │   ├── repository/
    │   │   ├── UserRepository.java         ← JpaRepository for users
    │   │   ├── ExpenseRepository.java      ← JpaRepository + custom @Query
    │   │   └── BudgetRepository.java       ← JpaRepository for budget
    │   ├── model/
    │   │   ├── User.java                   ← @Entity → users table
    │   │   ├── Expense.java                ← @Entity → expenses table
    │   │   └── Budget.java                 ← @Entity → budget table
    │   └── dto/
    │       └── ExpenseDTO.java             ← Request / Response classes
    └── resources/
        └── application.properties          ← DB config, JPA settings
```

---

## Setup & Run Instructions

### Prerequisites
- Java 21
- MySQL 8.0 running locally
- Maven
- Postman

### Step 1 — Create MySQL Database

```sql
CREATE DATABASE expense_tracker;
```

Tables are created automatically by Hibernate on first run.

### Step 2 — Configure application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### Step 3 — Run

```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080`

---

## API Endpoints

Base URL: `http://localhost:8080`

### User Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| POST | `/api/users/login` | Login |

**Register**
```json
POST /api/users/register
{
  "name": "Pranav",
  "email": "pranav@gmail.com",
  "password": "1234"
}
```
Response:
```json
{ "success": true, "message": "Registration successful", "data": { "userId": 1, "name": "Pranav", "email": "pranav@gmail.com" } }
```

---

### Expense Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/expenses/add` | Add new expense |
| GET | `/api/expenses/user/{userId}` | Get all expenses |
| PUT | `/api/expenses/update/{expenseId}` | Update an expense |
| DELETE | `/api/expenses/delete/{expenseId}` | Delete an expense |
| GET | `/api/expenses/report/monthly/{userId}` | Monthly report |
| POST | `/api/expenses/budget` | Set budget limit |
| PUT | `/api/expenses/budget/update/{userId}` | Update budget limit |

**Add Expense**
```json
POST /api/expenses/add
{ "userId": 1, "amount": 450.00, "category": "Food", "description": "Lunch" }
```

**Update Expense**
```json
PUT /api/expenses/update/1
{ "amount": 500.00, "category": "Food", "description": "Dinner" }
```

**Monthly Report**
```
GET /api/expenses/report/monthly/1
```
Response:
```json
{
  "success": true,
  "data": {
    "breakdown": [
      { "category": "Food", "total": 2400.0 },
      { "category": "Travel", "total": 800.0 }
    ],
    "grandTotal": 3200.0,
    "budgetLimit": 5000.0,
    "budgetStatus": "WITHIN LIMIT"
  }
}
```

**Set Budget**
```json
POST /api/expenses/budget
{ "userId": 1, "monthlyLimit": 5000.0 }
```

**Update Budget**
```json
PUT /api/expenses/budget/update/1
{ "monthlyLimit": 8000.0 }
```

---

## Database Schema

Hibernate auto-generates these tables from `@Entity` classes:

```sql
CREATE TABLE users (
    user_id   INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50)  NOT NULL,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL
);

CREATE TABLE expenses (
    expense_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT    NOT NULL,
    amount       DOUBLE NOT NULL,
    category     VARCHAR(30) NOT NULL,
    description  VARCHAR(100),
    expense_date DATE   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE budget (
    user_id       INT    PRIMARY KEY,
    monthly_limit DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

---

## Key Spring Annotations Used

| Annotation | Purpose |
|---|---|
| `@SpringBootApplication` | Entry point, auto-configuration |
| `@RestController` | REST controller, all methods return JSON |
| `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` | HTTP method mapping |
| `@PathVariable` | Reads `{id}` from URL |
| `@RequestBody` | Converts JSON body to Java object |
| `@Service` | Business logic layer bean |
| `@Repository` | Data layer bean |
| `@Autowired` | Spring dependency injection |
| `@Entity` | Hibernate maps class to DB table |
| `@ManyToOne` / `@OneToMany` | Table relationship mapping |
| `@Query` | Custom JPQL query in repository |

---

## Future Improvements

- [ ] JWT Authentication with Spring Security
- [ ] BCrypt password hashing
- [ ] Pagination for expense list
- [ ] Cloud deployment on Railway / Render

---

## Author

**Pranav Kharage**
- GitHub: [@Pranavkharage](https://github.com/Pranavkharage)
- LinkedIn: [pranav-kharage](https://www.linkedin.com/in/pranav-kharage-824354258/)
- Email: pranavkharage21@gmail.com
