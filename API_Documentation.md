# Fortress API Documentation

---

## Base URL ( Local development )

```
http://localhost:8080
```

---

# USER MODULE

---

## Get All Users

**GET** `/users`

### Response

Returns list of all users

---

## Create User

**POST** `/users`

### Request Body

```json
{
  "userName": "admin",
  "password": "123",
  "role": "ADMIN"
}
```

### Response

```
200 OK
User created successfully
```

---

## Get User by ID

**GET** `/users/{id}`

### Example

```
GET /users/1
```

---

## Login

**POST** `/users/login`

### Request Body

```json
{
  "userName": "admin",
  "password": "123"
}
```

### Response

Returns user object

### Notes

- Login is session-less (no JWT/token)
- Fails if user is inactive

---

## Update User Status (Activate / Deactivate)

**PUT** `/users/{id}/status`

### Query Parameters

- `isActive` → true / false
- `modifierID` → Admin user ID

### Example

```
PUT /users/2/status?isActive=false&modifierID=1
```

### Response

```
User status updated successfully
```

---

## Update User

**PUT** `/users/{id}`

### Query Parameters

- `modifierID` → Admin user ID

### Request Body

```json
{
  "userName": "newName",
  "role": "ANALYST"
}
```

### Example

```
PUT /users/2?modifierID=1
```

---

## Delete User

**DELETE** `/users/{id}`

### Query Parameters

- `modifierID` → Admin user ID

### Example

```
DELETE /users/2?modifierID=1
```

---

# TRANSACTION MODULE

---

## Create Transaction

**POST** `/transactions`

### Query Parameters

- `requesterID` → User performing the action

### Request Body

```json
{
  "userID": "1",
  "amount": 5000,
  "type": "INCOME",
  "category": "Salary",
  "date": "2026-04-01",
  "notes": "Monthly salary"
}
```

---

## Get Transaction by ID

**GET** `/transactions/{id}`

### Example

```
GET /transactions/1
```

---

## Get User Transactions (with Filtering)

**GET** `/transactions/user/{userID}`

---

### Optional Query Parameters

- `type` → INCOME / EXPENSE
- `category` → e.g. Food
- `startDate` → yyyy-mm-dd
- `endDate` → yyyy-mm-dd

---

### Examples

#### All transactions

```
GET /transactions/user/1
```

#### Filter by type

```
GET /transactions/user/1?type=EXPENSE
```

#### Filter by category

```
GET /transactions/user/1?category=Food
```

#### Combined filter

```
GET /transactions/user/1?type=EXPENSE&category=Food
```

#### Date range

```
GET /transactions/user/1?startDate=2026-04-01&endDate=2026-04-30
```

---

## Update Transaction

**PUT** `/transactions/{id}`

### Query Parameters

- `requesterID` → User performing action

### Request Body (partial allowed)

```json
{
  "amount": 6000,
  "category": "Updated Salary"
}
```

---

## Delete Transaction

**DELETE** `/transactions/{id}`

### Query Parameters

- `requesterID` → User performing action

---

# DASHBOARD / ANALYTICS

---

## Get Dashboard Summary

**GET** `/transactions/dashboard/{userID}`

### Example

```
GET /transactions/dashboard/1
```

---

## Response

```json
{
  "totalIncome": 52000,
  "totalExpense": 2800,
  "netBalance": 49200,
  "categoryBreakdown": {
    "Food": 800,
    "Transport": 800,
    "Entertainment": 1200
  },
  "recentTransactions": [
    {
      "transactionID": "1",
      "amount": 5000
    }
  ],
  "monthlyTrends": {
    "2026-04": 2800
  }
}
```

---

# ACCESS CONTROL (RBAC)

| Role    | Permissions   |
| ------- | ------------- |
| VIEWER  | Read-only     |
| ANALYST | Read + Update |
| ADMIN   | Full access   |

---

# ERROR HANDLING

Common error scenarios:

- Invalid role → `Invalid role`
- Invalid transaction type → `Invalid transaction type`
- Unauthorized action → `Access denied`
- User not found → `User not found`
- Inactive user → `User is inactive`

---

# NOTES

- Dates must follow format: `yyyy-mm-dd`
- Role and type values are case-insensitive
- Filtering parameters are optional
- Partial updates are supported for transactions
- System uses in-memory storage (data resets on restart)
- Tested using Postman

---

# SUMMARY

This API supports:

- User management with role-based access control
- Secure authentication (hashed passwords using SHA-256)
- Transaction CRUD with filtering
- Financial analytics and trends

---
