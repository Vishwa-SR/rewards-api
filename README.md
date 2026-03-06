# Rewards API – Spring Boot Project

## 📌 Overview

The Rewards API is a Spring Boot–based RESTful application that calculates reward points for customers based on their transactions.

The system evaluates transactions over a configurable time period (default: last 3 months) and computes reward points according to defined business rules.

This project demonstrates:

- REST API design using Spring Boot  
- Clean layered architecture  
- Validation handling  
- Exception handling  
- Logging using Log4j2 + AOP  
- Unit testing using JUnit, Mockito & MockMvc  

---

## 📌 Tech Stack

- Java 17  
- Spring Boot  
- Spring MVC  
- Spring Data JPA  
- MySQL  
- Log4j2 (Logging)  
- JUnit 5  
- Mockito  
- MockMvc  
- Maven  

---

##  📌 Project Structure

com.rewards.api
 ├── controller        → REST controllers  
 ├── service           → business logic implementation  
 ├── repository        → database access layer  
 ├── entity            → JPA entities  
 ├── dto               → response models  
 ├── exception         → custom exceptions + handler  
---

## 📌 Business Logic

Reward points are calculated based on transaction amount:

- If amount > 100 → 2 points per dollar above 100 + 50 points
- If amount between 50–100 → 1 point per dollar above 50
- If amount ≤ 50 → 0 points

The API calculates:

- Monthly reward points  
- Total reward points  

---

## 📌 API Endpoint

###  Get Rewards

GET /rewards/{customerId}
---

## 📌 Query Parameters (Optional)

| Parameter | Description |
|-----------|-------------|
| months    | Fetch rewards for last N months |
| startDate | Start date (yyyy-MM-dd) |
| endDate   | End date (yyyy-MM-dd) |

---

## 📌 Example Requests (Postman)

### 🔹 Default (Last 3 Months)

GET http://localhost:8765/rewards/1
  Fetches rewards for last 3 months automatically.

---

### 🔹 By Months

GET http://localhost:8765/rewards/1?months=2
  Fetches rewards for last 2 months.

---

### 🔹 By Date Range

GET http://localhost:8765/rewards/1?startDate=2026-01-01&endDate=2026-03-01
  Fetches rewards between selected dates.

---

## 📌 Validation Cases

| Scenario | Result |
|----------|--------|
| Invalid customerId | CUSTOMER_NOT_FOUND error |
| No transactions found | NO_TRANSACTION error |
| Invalid month (>12) | Validation error |
| Invalid date range | INVALID_DATE error |
| Both month + date params | INVALID_PARAMS error |

---

## 📌 Error Handling

Implemented using:

- Custom Exception → Rewardexception  
- Global Exception Handler → @RestControllerAdvice  

Example response:

{
  "errorMessage": "Invalid customer Id",
  "errorCode": 404,
  "timestamp": "2026-03-06T10:30:00"
}
---

## 📌 Logging

Logging implemented using:

- Log4j2  
- AOP (Aspect-Oriented Programming)

Logs capture:

- API calls  
- Exceptions  
- Validation failures  

Log file:

log/ErrorLog.log
---

## 📌 Testing

Unit testing implemented using:

- JUnit  
- Mockito  
- MockMvc  

### Covered Scenarios

✔ Successful reward calculation  
✔ Invalid customer  
✔ No transactions  
✔ Invalid month  
✔ Invalid date range  

---

## 📌 Database Setup

### MySQL Setup

### 📌 Initialize Tables

Database schema and sample data scripts are available in: ### src/main/resources

Run the SQL scripts from the resources folder to create tables and insert sample data.

 application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/rewards_db
spring.datasource.username=root
spring.datasource.password=root
---

## 📌 How to Run

1. Clone repository  
2. Configure MySQL database  
3. Run Spring Boot application  
4. Test endpoints using Postman  

---

  

Developed as part of Spring Boot backend learning
