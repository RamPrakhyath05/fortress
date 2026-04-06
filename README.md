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

This aligns with the assignment’s goal of evaluating backend engineering thinking, not just functionality.

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
Storage Layer (In-memory / Database)
```

---

## 📦 Project Structure

```
com.fortress
├── controller
├── model
├── repository
├── service
├── util
└── FortressApplication.java
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

- **Admin** → Full system access
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
- Monthly trend of transactions

---

## 🔐 Access Control (RBAC)

Role-based restrictions are enforced at the **service layer**:

- **Viewer** → Cannot create, update, or delete
- **Analyst** → Can view and update data
- **Admin** → Full access including user management

All access checks are validated before executing business logic.

## 🛠 Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Architecture:** Layered (Controller → Service → Repository)
- **Storage:** In-memory (HashMap)

---

## 🔒 Security

- Passwords are hashed using SHA-256
- No plain-text password storage
- Authentication handled at service layer
- Designed for future JWT integration

---

## ⚠️ Validation & Error Handling

- Basic input validation implemented
- Invalid operations handled using exceptions
- Meaningful error messages returned to client
- Designed for extension into global exception handling

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

- In-memory storage is used for simplicity
- Authentication is simplified (no tokens)
- Focus is on backend design and logic, not production deployment

---

## 🚧 Future Enhancements

- JWT-based authentication
- Pagination and sorting
- Integration Testing

---

## 💡 Author

**Ram Prakhyath Annamareddy**

---
