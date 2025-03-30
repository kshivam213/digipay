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

**Response Body

```json
{
    "success": true,
    "message": "User created successfully",
    "data": {
        "id": "1f96e31265e640",
        "name": "MS Dhoni",
        "balance": 12000.00
    }
}
```




    