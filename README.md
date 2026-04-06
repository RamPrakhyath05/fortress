# Fortress

A Finance Data Processing and Access Control Backend Application

---

## 🚀 Overview

**Fortress** is a backend system built using **Spring Boot** to manage financial data with structured access control and clean architecture.

The system simulates a real-world **finance dashboard backend**, where multiple users interact with financial records based on their roles and permissions.

This project focuses on:

- Clean backend architecture
- RESTful API design
- Role-based access control (RBAC)
- Data processing and aggregation logic
- Maintainable and scalable system design

---

## 🎯 Objective

To design and implement a backend system that demonstrates:

- Strong data modeling
- Clear separation of concerns
- Well-structured APIs
- Business logic implementation
- Role-based access control

---

## 🧱 System Architecture

```
Client (Postman / Frontend)
        ↓
REST Controllers (Spring Boot)
        ↓
Service Layer (Business Logic + Access Control)
        ↓
Repository Layer (Data Access)
        ↓
SQLite Database
```

---

## 📦 Project Structure & Directory

```
fortress
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── fortress
│   │   │           ├── controller
│   │   │           │   ├── TransactionController.java
│   │   │           │   ├── TransactionRequest.java
│   │   │           │   ├── UserController.java
│   │   │           │   └── UserRequest.java
│   │   │           │
│   │   │           ├── exception
│   │   │           │   ├── BadRequestException.java
│   │   │           │   ├── NotFoundException.java
│   │   │           │   ├── UnauthorizedException.java
│   │   │           │   └── GlobalExceptionHandler.java
│   │   │           │
│   │   │           ├── model
│   │   │           │   ├── Role.java
│   │   │           │   ├── Transaction.java
│   │   │           │   ├── TransactionType.java
│   │   │           │   └── User.java
│   │   │           │
│   │   │           ├── repository
│   │   │           │   ├── TransactionRepository.java
│   │   │           │   └── UserRepository.java
│   │   │           │
│   │   │           ├── service
│   │   │           │   ├── TransactionService.java
│   │   │           │   └── UserService.java
│   │   │           │
│   │   │           ├── util
│   │   │           │   └── PasswordHasher.java
│   │   │           │
│   │   │           └── FortressApplication.java
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── java
│           └── com
│               └── fortress
│                   └── service
│                       ├── TransactionServiceTest.java
│                       └── UserServiceTest.java
│
├── API_Documentation.md
├── README.md
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

## 👤 User & Role Management

### Features:

- Create, update, and delete users
- Assign roles to users
- Enable/disable users (isActive = true/false)
- Authenticate users (login)
- Secure password storage using hashing

### Roles:

- **ADMIN** → Full system access
- **ANALYST** → Read + Update
- **VIEWER** → Read-only

---

## 💰 Transaction Management

Each transaction includes:

- Amount
- Type (INCOME / EXPENSE)
- Category
- Date
- Notes

### Supported Operations:

- Create transactions
- View transactions
- Update transactions (partial updates supported)
- Delete transactions
- Filter transactions by:
  - Type
  - Category
  - Date range

---

## 📊 Analytics

The system provides summary-level insights:

- Total income
- Total expenses
- Net balance
- Category-wise breakdown
- Recent transactions
- Monthly transaction trends

---

## 🔐 Access Control (RBAC)

Role-based restrictions are enforced at the **service layer**:

- **VIEWER** → Cannot create, update, or delete
- **ANALYST** → Can read and update
- **ADMIN** → Full access including user management

---

## 🛠 Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Architecture:** Layered (Controller → Service → Repository)
- **Database:** SQLite

---

## 🔒 Security

- Passwords are hashed using **SHA-256**
- No plain-text password storage
- Authentication handled at service layer
- Designed for future JWT integration

---

## ⚠️ Validation & Error Handling

- Input validation for role, transaction type, and date format
- Exception-based error handling
- Standard HTTP status codes used:
  - 200 → Success
  - 400 → Bad Request
  - 403 → Forbidden
  - 404 → Not Found

---

## ⚙️ How to Run

```bash
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## 📌 Assumptions

- Authentication is simplified (no JWT/token)
- Focus is on backend design and logic, not production deployment
- SQLite is used as a lightweight local database

---

## 🚧 Future Enhancements

- JWT-based authentication
- Pagination and sorting
- Integration Testing

---

## 💡 Author

**Ram Prakhyath Annamareddy**

---
