# Rewards API – Spring Boot Project

## 📌 Overview

The Rewards API is a Spring Boot–based RESTful application that calculates reward points for customers based on their transactions.

The system evaluates transactions over a configurable time period (default: last 3 months) and computes reward points according to defined business rules.


---

## 📌 Key Features

RESTful API design using Spring Boot

Clean layered architecture (Controller → Service → Repository)

Global exception handling using @RestControllerAdvice

Structured logging using Spring Boot default logging (Logback)

Unit testing with JUnit, Mockito, and MockMvc

Well-organized documentation with test and API proof screenshots



---

## 📌 Tech Stack

Java 17

Spring Boot

Spring MVC

Spring Data JPA

MySQL

Spring Boot Default Logging (Logback)

JUnit 5

Mockito

MockMvc

Maven



---

## 📌 Project Structure
```
com.rewards.api  
├── controller      → REST controllers  
├── service         → business logic 
├── repository      → database access layer  
├── entity          → JPA entities  
├── dto             → response models  
├── exception       → custom exceptions & global handler  
├── util            → validation utilities
```

---

## 📌 Business Logic

 ### Reward points are calculated based on transaction amount:
```
Amount > 100 → (2 × (amount - 100)) + 50 points

Amount between 50–100 → (amount - 50) points

Amount ≤ 50 → 0 points
````

 ### The API calculates:

 Monthly reward points

 Total reward points



---

## 📌 API Endpoint

 ### Get Rewards

GET /rewards/{customerId}


---

## 📌 Query Parameters (Optional)

| Parameter | Description |
|----------|------------|
| months | Fetch rewards for last N months |
| startDate | Start date (yyyy-MM-dd) |
| endDate | End date (yyyy-MM-dd) |

---

## 📌 Example Requests
```
Default (Last 3 Months)

GET http://localhost:8765/rewards/1  

By Months

GET http://localhost:8765/rewards/1?months=2  

By Date Range

GET http://localhost:8765/rewards/1?startDate=2026-01-01&endDate=2026-03-01  

````
---

## 📌 Example Responses
```
Successful Response

{  
  "customerId": 1,  
  "customerName": "Vishwa",  
  "monthlyRewards": {  
    "Jan-2026": 395.8,  
    "Feb-2026": 497.7  
  },  
  "totalRewards": 893.5  
}


---

Invalid Month

{  
  "errorMessage": "Invalid months value: 33. It should be between 1 and 12",  
  "errorCode": 400,  
  "timestamp": "2026-03-17T08:37:29"  
}


---

Invalid Date Range

{  
  "errorMessage": "Invalid date range: startDate must be before endDate",  
  "errorCode": 400,  
  "timestamp": "2026-03-17T08:38:19"  
}


---

Customer Not Found

{  
  "errorMessage": "Customer not found for customerId: 11",  
  "errorCode": 404,  
  "timestamp": "2026-03-17T08:38:42"  
}

```
---

## 📌 Validation Rules

| Scenario | Result |
|----------|--------|
| Invalid customerId | NotFoundException |
| No transactions found | NotFoundException |
| Invalid months (>12 or <1) | BadRequestException |
| Invalid date range | BadRequestException |
| Both months + date range provided | BadRequestException |
| Only startDate or endDate provided | BadRequestException |

---

## 📌 Error Handling

 ### Custom Exceptions:

 BadRequestException  
 NotFoundException  

 ### Global Handler:

 @RestControllerAdvice  



---

## 📌 Logging

 Logging is implemented using Spring Boot’s default logging framework (Logback).

 ### Logs include:

 Request processing flow  
 Validation failures  
 Exception details  


---

## 📌 Testing

 ### Tools Used

 JUnit 5  
 Mockito  
 MockMvc  

### Covered Scenarios

 Successful scenarios (default, months, date range)  
 Invalid inputs  
 Customer not found  
 No transactions  
 Validation failures  



---

## 📌 Documentation 

Project includes a docs/ folder with:
```
docs/  
├── build-result/  
│   └── maven-build-success.png  
├── postman-results/  
│   ├── postman-default-success.png  
│   ├── postman-monthsAsParam-success.png  
│   └── postman-daterangeAsParam-success.png  
├── test-results/  
│   ├── controller-test-success.png  
│   └── service-test-success.png  
├── sample-request-response.md  
```

---

## 📌 Database Setup

 ### MySQL Configuration
```
spring.datasource.url=jdbc:mysql://localhost:3306/rewards_db  
spring.datasource.username=YOUR_USERNAME  
spring.datasource.password=YOUR_PASSWORD  
```
Initialize Tables

SQL script available in:

src/main/resources/TableScript.sql  


---

## 📌 How to Run

1. Clone the repository  
2. Configure MySQL database (use SQL script from TableScript.sql)  
3. Run Spring Boot application  
4. Test endpoints using Postman (refer sample-request-response.txt)  


---

## 📌 Author

Developed as part of backend learning and training.


---
