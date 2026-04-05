# Fortress

A Finance Data Processing and Access Control Backend Application

## 🚀 Overview

**Fortress** is a backend system built using **Spring Boot** to manage financial data with structured access control and clean architecture.

The system simulates a real-world **finance dashboard backend**, where multiple users interact with financial records based on their roles and permissions.

This project focuses on:

- Clean backend architecture
- API design using REST principles
- Role-based access control
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

This aligns with the assignment’s goal of evaluating backend engineering thinking, not just functionality.

---

## 🧱 System Architecture

The system follows a layered architecture:

```
Client (Postman / Frontend)
        ↓
REST Controllers (Spring Boot)
        ↓
Service Layer (Business Logic + Access Control)
        ↓
Repository Layer (Data Access)
        ↓
Storage Layer (In-memory / Database)
```

---

## 📦 Project Structure

```
com.fortress
├── controller
│   ├── UserController.java
│   └── TransactionController.java
│
├── service
│   ├── UserService.java
│   ├── TransactionService.java
│   └── AnalyticsService.java
│
├── repository
│   ├── UserRepository.java
│   └── TransactionRepository.java
│
├── model
│   ├── User.java
│   └── Transaction.java
│
├── util
│   └── PasswordHasher.java
│
└── FortressApplication.java
```

---

## 👤 User & Role Management

### Features:

- Create and manage users
- Secure password storage using hashing
- Assign roles to users
- Enable/disable users
- Authenticate users

### Roles:

- **Admin** → Full system access (manage users & records)
- **Analyst** → View records and analytics
- **Viewer** → Read-only access

---

## 💰 Financial Records Management

Each transaction includes:

- Amount
- Type (Income / Expense)
- Category
- Date
- Notes

### Supported Operations:

- Create transactions
- View transactions
- Update transactions
- Delete transactions
- Filter by:
  - Date
  - Category
  - Type

---

## 📊 Analytics

The system provides summary-level insights:

- Total income
- Total expenses
- Net balance
- Category-wise breakdown
- Recent activity
- Trends (monthly / weekly)

This module focuses on **data aggregation logic**, not just CRUD operations.

---

## 🔐 Access Control

Role-based restrictions are enforced at the **service layer**:

- Viewer → Cannot modify data
- Analyst → Can view data and analytics
- Admin → Full access

Access checks are implemented before executing business logic.

---

## 🌐 API Design (Planned)

### User APIs

- `POST /users` → Create user
- `GET /users/{id}` → Get user details
- `DELETE /users/{id}` → Delete user

### Transaction APIs

- `POST /transactions` → Create transaction
- `GET /transactions` → Get all transactions
- `PUT /transactions/{id}` → Update transaction
- `DELETE /transactions/{id}` → Delete transaction

### Analytics APIs

- `GET /analytics/summary` → Get financial summary
- `GET /analytics/category` → Category-wise totals

---

## 🛠 Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Architecture:** Layered (Controller → Service → Repository)
- **Storage:** In-memory (HashMap) _(designed for easy DB integration)_

---

## 🔒 Security

- Passwords are stored using hashing (SHA-256)
- Plain-text passwords are never stored
- Authentication logic handled in service layer
- Designed for future upgrade to JWT-based authentication

---

## ⚙️ How to Run

```bash
mvn spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

## 🧪 Current Status

- ✅ User Management
- 🔄 Role-based Access Control
- ⏳ Financial Records Module
- ⏳ Analytics Module

---

## 🚧 Future Enhancements

- Database integration
- JWT authentication & authorization
- Pagination & filtering improvements
- API documentation (Swagger / OpenAPI)
- Unit & integration testing
- Rate limiting & validation improvements

---

## 📌 Assumptions

- In-memory storage is used for simplicity
- Authentication is simplified (no tokens yet)
- Focus is on backend architecture and logic rather than production readiness

---

## 💡 Author

**Ram Prakhyath Annamareddy**

---
