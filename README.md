# AssetFlow Enterprise ERP

## Local Setup Instructions

1. **Environment Configuration**
   Copy the example environment file to create your local configuration:
   ```bash
   cp .env.example .env
   ```
   
2. **Configure Database Credentials**
   Open `.env` and configure your PostgreSQL connection details:
   ```env
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=assetflow
   DB_USERNAME=postgres
   DB_PASSWORD=your_local_postgres_password
   ```

3. **Configure Security (JWT)**
   Update the `JWT_SECRET` in `.env` with a secure random string (minimum 64 characters for HS512):
   ```env
   JWT_SECRET=your_secure_random_secret_here...
   ```

4. **Start the Application**
   The application uses Spring Boot and can be started using Maven. It will automatically load the configuration from the environment (or use the defaults if running locally without a `.env` loader tool).

   *Note: Spring Boot natively resolves system environment variables. If you are using IntelliJ IDEA or VS Code, you can use an EnvFile plugin or directly specify the environment variables in your run configuration.*

   ```bash
   cd backend
   mvn spring-boot:run
   ```
