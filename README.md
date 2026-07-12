# AssetFlow Enterprise ERP (Odoo Hackathon 2026 Project)

Welcome to AssetFlow, a modern Enterprise ERP system developed for the **Odoo Hackathon 2026**. This repository contains both the backend and frontend components.

## Prerequisites

Ensure you have the following installed on your system before proceeding:
- **Java:** JDK 21
- **Maven:** 3.8+ (or use the included wrapper `mvnw`)
- **PostgreSQL:** 14 or newer
- **Node.js:** 18+ (for frontend)
- **npm:** (for frontend)

---

## Backend Setup Instructions

The backend is built with Spring Boot and uses PostgreSQL for the database.

### 1. Environment Configuration

The application is fully configurable via environment variables. Navigate to the backend directory and copy the provided template to create your local `.env` file:

```bash
cd backend
cp .env.example .env
```

### 2. Configure Database Credentials

Open your new `.env` file and configure the PostgreSQL connection details. You will need to create the database in PostgreSQL manually before starting the application:

```sql
-- In your psql console or database tool
CREATE DATABASE assetflow;
```

Update your `.env` file:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=assetflow
DB_USERNAME=postgres
DB_PASSWORD=your_local_postgres_password
```

### 3. Configure Security (JWT)

You must configure a strong JWT secret. For the `HS512` algorithm used by this application, the secret **must be a minimum of 64 characters long**.

Update the `JWT_SECRET` in `.env`:
```env
JWT_SECRET=your_secure_random_secret_here_must_be_at_least_64_characters_long
```
*Note: The application will fail to start if this secret is missing or less than 64 characters.*

### 4. Start the Application

The application uses Spring Boot and Flyway. When the application starts, Flyway will automatically run all database migrations (V1 through V19) to create the schema and seed initial roles.

To start the backend using Maven:

```bash
cd backend
mvn spring-boot:run
```
*(If you are using IntelliJ IDEA or VS Code, you can use an EnvFile plugin to load `.env`, or set the environment variables in your run configuration).*

### 5. API Documentation (Swagger)

Once the application has started successfully, you can view and interact with the REST API documentation using Swagger UI:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

You can use the `/api/v1/auth/signup` and `/api/v1/auth/login` endpoints directly from Swagger to test authentication.

---

## Frontend Setup Instructions

The frontend is built using Next.js and React.

### 1. Install Dependencies

Navigate to the frontend directory and install the required dependencies:

```bash
cd frontend
npm install
```

### 2. Configure Environment

Ensure that the frontend can communicate with the backend API. If you need to set a custom backend URL, you can usually do so in an environment file (e.g., `.env.local`). By default, the application may assume the backend is at `http://localhost:8080`.

### 3. Start the Development Server

To start the frontend application:

```bash
cd frontend
npm run dev
```

The frontend will typically be accessible at [http://localhost:3000](http://localhost:3000).
