# SmartPass – AI-Powered Digital Pass Management System

An intelligent digital pass management platform designed to simplify commuter pass applications, renewals, payments, and verification through automation and AI-powered features.

## About the Project

SmartPass aims to modernize the traditional commuter pass management process by providing a centralized digital platform for users and administrators.

The system allows users to apply for commuter passes, manage their applications, make payments, track renewal history, and access their pass information. It also incorporates AI-powered features to support application review, risk assessment, insights, and recommendations.

## Key Features

- **User Authentication:** User registration and login.
- **Digital Pass Management:** Apply for and manage commuter passes.
- **Pass Renewal:** Renew passes and view renewal history.
- **Payment Management:** Manage payment operations and receipts.
- **Identity Verification:** Support identity verification workflows.
- **AI-Powered Insights:** Generate insights and recommendations.
- **Risk Assessment:** Identify potential application risks.
- **Application Review:** Support administrative application review.
- **Admin Dashboard:** Manage applications and monitor system activities.
- **Smart Alerts:** Display relevant alerts and application updates.

## Technology Stack

| Component | Technologies |
|---|---|
| Frontend | React.js, Vite, JavaScript |
| UI | Bootstrap, CSS |
| Backend | Java, Spring Boot |
| Database | MySQL |
| API Communication | Axios, REST APIs |
| AI Integration | Google Gemini API |
| Build Tool | Maven |

## Project Structure

```text
SmartPass/
│
├── smartpass/                  # Spring Boot Backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── pom.xml
│
├── smartpass-frontend/         # React Frontend
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   └── styles/
│   ├── package.json
│   └── vite.config.js
│
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

Install the following before running the application:

- Java 17 or compatible version required by the project
- Node.js and npm
- MySQL Server
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/SreenaKannan/SmartPass.git
cd SmartPass
```

### 2. Configure the Database

Create a MySQL database:

```sql
CREATE DATABASE smartpass;
```

Configure the following environment variables for the backend:

```text
DB_URL=jdbc:mysql://localhost:3306/smartpass
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key
SMARTPASS_UPLOAD_DIR=uploads
```

Never commit actual database passwords or API keys to GitHub.

### 3. Run the Backend

Open a terminal in the project root and configure the required environment variables.

For PowerShell, for example:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/smartpass"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:GEMINI_API_KEY="your_gemini_api_key"
```

Then start the backend:

```powershell
cd smartpass
.\mvnw spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### 4. Run the Frontend

Open another terminal:

```powershell
cd smartpass-frontend
npm install
npm run dev
```

Open the local URL displayed by Vite, usually:

```text
http://localhost:5173
```

## Future Enhancements

- Improved analytics and reporting.
- Enhanced security and authentication.
- Further automation of commuter pass workflows.
- Additional AI-powered decision-support features.

## Author

**Sreena Kannan**

Computer Science and Business Systems Student

GitHub: [SreenaKannan](https://github.com/SreenaKannan)

---

*SmartPass – Making commuter pass management smarter, simpler, and more efficient.*
