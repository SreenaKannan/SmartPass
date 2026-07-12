# 🚌 SmartPass – Online Bus & Metro Smart Card Management System

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple?logo=bootstrap)
![REST API](https://img.shields.io/badge/REST-API-success)
![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apachemaven)

---

## 🚍 Project Overview

SmartPass is a full-stack web application designed to simplify **Bus and Metro Smart Card Management** through a digital platform. The application enables commuters to register, apply for travel cards, make online payments, renew passes, search cards, and track renewal history.

The system also provides an **Admin Dashboard** where administrators can approve or reject applications, manage commuter cards, monitor payments, and view dashboard statistics.

This project demonstrates modern full-stack development using **React.js**, **Spring Boot**, **REST APIs**, and **MySQL**.

---

# ✨ Features

## 👤 User Module

- User Registration
- Secure Login
- Apply for Bus Smart Card
- Apply for Metro Smart Card
- Online Card Renewal
- View My Cards
- Search Smart Card
- Online Payment
- Renewal History

---

## 👨‍💼 Admin Module

- Dashboard Analytics
- Approve Card Applications
- Reject Card Applications
- Block Smart Cards
- Delete Smart Cards
- Monitor Users
- Monitor Payments

---

# 🏗️ System Architecture

```text
                   User
                     │
                     ▼
           React.js Frontend
                     │
           Axios REST API Calls
                     │
                     ▼
         Spring Boot Backend
                     │
      Spring Data JPA (Hibernate)
                     │
                     ▼
              MySQL Database
```

---

# 🛠️ Tech Stack

## Frontend

- React.js
- HTML5
- CSS3
- JavaScript (ES6)
- Bootstrap 5
- Axios

---

## Backend

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- REST APIs

---

## Database

- MySQL

---

## Build Tools

- Maven

---

## Development Tools

- VS Code
- Postman
- Git
- GitHub

---

# 📂 Project Structure

```text
SmartPass
│
├── smartpass
│   │
│   ├── src
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── entity
│   │   ├── dto
│   │   └── config
│   │
│   └── pom.xml
│
├── smartpass-frontend
│   │
│   ├── src
│   │   ├── assets
│   │   ├── components
│   │   ├── pages
│   │   ├── services
│   │   └── styles
│   │
│   └── package.json
│
└── README.md
```

---

# 🚀 Installation Guide

## Clone Repository

```bash
git clone https://github.com/SreenaKannan/SmartPass.git
```

---

## Backend Setup

```bash
cd smartpass
mvn spring-boot:run
```

Backend runs at

```
http://localhost:8080
```

---

## Frontend Setup

```bash
cd smartpass-frontend
npm install
npm run dev
```

Frontend runs at

```
http://localhost:5173
```

---

# 🗄️ Database Configuration

Create a MySQL database:

```sql
CREATE DATABASE smartpass;
```

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartpass
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# 📡 REST API Endpoints

## User

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/users/register` | Register User |
| POST | `/api/users/login` | Login |

---

## Smart Card

| Method | Endpoint |
|---------|----------|
| POST | `/api/cards/create` |
| GET | `/api/cards` |
| GET | `/api/cards/{id}` |
| PUT | `/api/cards/update/{id}` |
| PUT | `/api/cards/renew/{id}` |
| PUT | `/api/cards/block/{id}` |
| DELETE | `/api/cards/{id}` |

---

## Payment

| Method | Endpoint |
|---------|----------|
| POST | `/api/payments/pay` |
| GET | `/api/payments` |

---

## Renewal History

| Method | Endpoint |
|---------|----------|
| GET | `/api/renewals/{cardId}` |

---

## Dashboard

| Method | Endpoint |
|---------|----------|
| GET | `/api/dashboard` |

---

# 📷 Application Screenshots

Create an **images** folder in the repository and add screenshots.

```text
images
│
├── login.png
├── register.png
├── dashboard.png
├── apply-card.png
├── my-cards.png
├── payment.png
├── search.png
├── renewal-history.png
└── admin.png
```

Example:

```markdown
## Login

![Login](images/login.png)

---

## Dashboard

![Dashboard](images/dashboard.png)

---

## Apply Card

![Apply Card](images/apply-card.png)

---

## My Cards

![My Cards](images/my-cards.png)

---

## Payment

![Payment](images/payment.png)

---

## Renewal History

![Renewal History](images/renewal-history.png)

---

## Admin Dashboard

![Admin](images/admin.png)
```

---

# 🔒 Security Features

- User Authentication
- Role-Based Access (User/Admin)
- Secure REST API Communication
- Input Validation
- Database Persistence using JPA

---

# 🎯 Key Functionalities

- Digital Smart Card Management
- Bus & Metro Pass Application
- Online Payment Management
- Card Renewal System
- Renewal History Tracking
- Dashboard Analytics
- Admin Management

---

# 🔮 Future Enhancements

- JWT Authentication
- QR Code Verification
- Email Notifications
- SMS Alerts
- UPI Payment Gateway
- Google Maps Integration
- Mobile Application (Android & iOS)
- AI-based Travel Analytics
- Cloud Deployment (AWS / Azure)

---

# 👩‍💻 Developer

## **Sreena Kannan**

**Final-Year Computer Science and Business Systems Student**

### Skills

- Java
- Spring Boot
- React.js
- HTML
- CSS
- JavaScript
- MySQL
- REST APIs
- Git & GitHub

---

# ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub.

It motivates further development and improvements.
