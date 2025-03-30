# Digipay - Payment Service

## Local Setup
To set up and run the **Digipay** payment service locally, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/kshivam213/digipay.git

2. Navigate to the payment_service directory:
    ```sh
    cd payment_service

3. Build the project using Gradle:
    ```sh
   ./gradlew clean build

4. Start the application:
    ```sh
   Run DigipayApplication.java

## API Specification

### 1. Create User

**Endpoint**

POST http://localhost:8080/api/users

**Request Body**

```json
{
    "name": "John Doe",
    "balance": 12000.00,
    "currency": "USD"
}
```

**Response Body**

```json
{
   "success": true,
   "message": "User created successfully",
   "data": {
      "id": "3d50d8ea314148",
      "name": "John Doe",
      "currency": "INR",
      "balance": 12000.00
   }
}
```

### 2. Fetch User
GET http://localhost:8080/api/users/3d50d8ea314148

**Response Body**

```json
{
   "success": true,
   "message": "User retrieved successfully",
   "data": {
      "id": "3d50d8ea314148",
      "name": "MS Dhoni",
      "currency": "INR",
      "balance": 12000.00
   }
}
```

### 3. Make Payment

POST http://localhost:8080/api/transaction


**Request Body**

```json
{
   "senderId": "2909cc4502374d",
   "receiverId": "1f96e31265e640",
   "amount": 500
}
```

**Response Body**

```json
{
   "success": true,
   "message": "Successfully made transaction",
   "data": {
      "transactionId": "853eb2a702c349",
      "senderId": "2909cc4502374d",
      "receiverId": "1f96e31265e640",
      "amount": 500,
      "status": "SUCCESS",
      "remark": null
   }
}
```

