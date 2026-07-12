# AssetFlow Enterprise ERP (Backend)

Welcome to the backend component of AssetFlow, a modern Enterprise ERP system.

## Prerequisites

Ensure you have the following installed on your system before proceeding:
- **Java:** JDK 21
- **Maven:** 3.8+ (or use the included wrapper `mvnw`)
- **PostgreSQL:** 14 or newer

## Local Setup Instructions

### 1. Environment Configuration

The application is fully configurable via environment variables. Copy the provided template to create your local `.env` file:

```bash
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
