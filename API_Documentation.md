# 📘 Fortress API Documentation

---

## 🌐 Base URL

```
http://localhost:8080
```

---

# 👤 USER MODULE

---

## 🔹 Get All Users

**GET** `/users`

### Response

- **200 OK** → Returns list of all users

---

## 🔹 Create User

**POST** `/users`

### Request Body

```json
{
  "userName": "admin",
  "password": "123",
  "role": "ADMIN"
}
```

### Responses

- **200 OK**

  ```
  User created successfully
  ```

- **400 Bad Request**

  ```
  Invalid role
  ```

- **400 Bad Request**

  ```
  Username already exists
  ```

---

## 🔹 Get User by ID

**GET** `/users/{id}`

### Responses

- **200 OK** → Returns user object
- **404 Not Found**

  ```
  User not found
  ```

---

## 🔹 Login

**POST** `/users/login`

### Request Body

```json
{
  "userName": "admin",
  "password": "123"
}
```

### Responses

- **200 OK** → Returns user object
- **404 Not Found**

  ```
  User not found
  ```

- **400 Bad Request**

  ```
  Invalid password
  ```

- **403 Forbidden**

  ```
  User is inactive
  ```

---

## 🔹 Update User Status

**PUT** `/users/{id}/status`

### Query Parameters

- `isActive` → true / false
- `modifierID` → Admin ID

### Responses

- **200 OK**

  ```
  User status updated successfully
  ```

- **403 Forbidden**

  ```
  Access denied, ADMIN only
  ```

- **403 Forbidden**

  ```
  Inactive user cannot perform actions
  ```

- **404 Not Found**

  ```
  User not found
  ```

- **404 Not Found**

  ```
  Requester not found
  ```

---

## 🔹 Update User

**PUT** `/users/{id}`

### Query Parameters

- `modifierID`

### Request Body

```json
{
  "userName": "newName",
  "role": "ANALYST"
}
```

### Responses

- **200 OK**

  ```
  User details updated successfully
  ```

- **400 Bad Request**

  ```
  Invalid role
  ```

- **400 Bad Request**

  ```
  Username already taken
  ```

- **403 Forbidden**

  ```
  Access denied, ADMIN only
  ```

- **404 Not Found**

  ```
  User not found
  ```

---

## 🔹 Delete User

**DELETE** `/users/{id}`

### Query Parameters

- `modifierID`

### Responses

- **200 OK**

  ```
  User Deleted Successfully
  ```

- **403 Forbidden**

  ```
  Access denied, ADMIN only
  ```

- **404 Not Found**

  ```
  User not found
  ```

---

# 💰 TRANSACTION MODULE

---

## 🔹 Create Transaction

**POST** `/transactions`

### Query Parameters

- `requesterID`

### Request Body

```json
{
  "userID": "user-ab12cd",
  "amount": 5000,
  "type": "INCOME",
  "category": "Salary",
  "date": "2026-04-01",
  "notes": "Monthly salary"
}
```

### Responses

- **200 OK**

  ```
  Transaction created successfully
  ```

- **400 Bad Request**

  ```
  Invalid transaction type
  ```

- **400 Bad Request**

  ```
  Invalid date format
  ```

- **403 Forbidden**

  ```
  Access denied
  ```

- **403 Forbidden**

  ```
  Inactive user cannot perform actions
  ```

---

## 🔹 Get Transaction by ID

**GET** `/transactions/{id}`

### Responses

- **200 OK** → Returns transaction object
- **404 Not Found**

  ```
  Transaction not found
  ```

---

## 🔹 Get User Transactions (Filtering)

**GET** `/transactions/user/{userID}`

### Query Parameters (optional)

- `type` → INCOME / EXPENSE
- `category`
- `startDate` → yyyy-mm-dd
- `endDate` → yyyy-mm-dd

### Responses

- **200 OK** → Returns filtered transactions list

---

## 🔹 Update Transaction

**PUT** `/transactions/{id}`

### Query Parameters

- `requesterID`

### Request Body (partial allowed)

```json
{
  "amount": 6000,
  "category": "Updated Salary"
}
```

### Responses

- **200 OK**

  ```
  Transaction updated successfully
  ```

- **403 Forbidden**

  ```
  Access denied
  ```

- **404 Not Found**

  ```
  Transaction not found
  ```

---

## 🔹 Delete Transaction

**DELETE** `/transactions/{id}`

### Query Parameters

- `requesterID`

### Responses

- **200 OK**

  ```
  Transaction deleted successfully
  ```

- **403 Forbidden**

  ```
  Access denied, ADMIN only
  ```

- **404 Not Found**

  ```
  Transaction not found
  ```

---

# 📊 DASHBOARD / ANALYTICS

---

## 🔹 Get Dashboard Summary

**GET** `/transactions/dashboard/{userID}`

### Response

- **200 OK**

```json
{
  "totalIncome": 52000,
  "totalExpense": 2800,
  "netBalance": 49200,
  "categoryBreakdown": {
    "Food": 800,
    "Transport": 800
  },
  "recentTransactions": [
    {
      "transactionID": "transac-abc123",
      "amount": 5000
    }
  ],
  "monthlyTrends": {
    "2026-04": 2800
  }
}
```

---

# 🔐 ACCESS CONTROL (RBAC)

| Role    | Permissions                                      |
| ------- | ------------------------------------------------ |
| VIEWER  | Read-only                                        |
| ANALYST | Read + Create + Update                           |
| ADMIN   | Full access (including delete & user management) |

---

# ⚠️ ERROR HANDLING

| Status Code | Scenario                                             |
| ----------- | ---------------------------------------------------- |
| 400         | Invalid input (role, type, date, duplicate username) |
| 403         | Unauthorized action / inactive user                  |
| 404         | Resource not found                                   |
| 200         | Successful operation                                 |

---

# 🧠 NOTES

- User IDs follow format:

  ```
  user-xxxxxx
  ```

- Transaction IDs follow format:

  ```
  transac-xxxxxx
  ```

- Dates must follow:

  ```
  yyyy-mm-dd
  ```

- Role and transaction type are case-insensitive
- Partial updates are supported
- Data is persisted using **SQLite database**
- Passwords are hashed using **SHA-256**
- APIs tested using Postman

---

# 🚀 SUMMARY

This API provides:

- Role-based user management (RBAC)
- Secure authentication with hashed passwords
- Transaction management with filtering
- Financial analytics (income, expense, trends)
- Persistent storage using SQLite

---
