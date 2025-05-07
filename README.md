# Tiny URL Service

## Overview

Tiny URL Service is a robust, scalable RESTful API built with Quarkus and Java 21 for creating and managing shortened URLs. This service enables the creation of short, memorable URLs that redirect to longer destination URLs, with advanced features including time-limited availability, usage limits, and access attempt tracking.

## Features

- **URL Shortening**: Convert long URLs into short, readable codes
- **Expiration Management**: Set time limits for URL availability
- **Usage Limiting**: Configure URLs for one-time use or limit by number of uses
- **Access Attempt Tracking**: Limit the number of attempts to access a URL
- **Asynchronous API**: Non-blocking reactive design for high throughput
- **Comprehensive Logging**: Complete request/response logging with correlation IDs
- **OpenAPI Documentation**: Interactive API documentation with Swagger UI
- **Health Monitoring**: Built-in health checks and metrics

## Technology Stack

### Core Technologies
- **Java 21**: Latest LTS version of Java with enhanced features
- **Quarkus 3.9.0**: Supersonic Subatomic Java framework
- **Gradle 8.5**: Build automation tool

### Database
- **PostgreSQL**: Primary data storage for URL mappings
- **Hibernate Reactive**: ORM for reactive database access
- **PanacheQL**: Simplified entity management
- **Liquibase**: Database migration and version control

### Caching
- **Redis**: High-performance caching for frequently accessed URLs

### API & Documentation
- **RESTEasy Reactive**: JAX-RS implementation for RESTful API
- **SmallRye OpenAPI**: OpenAPI 3.0 specification implementation
- **Swagger UI**: Interactive API documentation

### Reactive Programming
- **Mutiny**: Reactive programming library for Java
- **Reactive Routes**: Non-blocking HTTP request handling

### Security
- **Quarkus Security**: Authentication and authorization
- **Input Validation**: Request validation with Hibernate Validator

### Monitoring & Observability
- **MicroProfile Metrics**: Application metrics collection
- **SmallRye Health**: Health check endpoints
- **Comprehensive Logging**: Request/response logging with MDC

### Testing
- **JUnit 5**: Testing framework
- **RestAssured**: API testing
- **Mockito**: Mocking framework

## Requirements

To build and run this application, you need:

- Java 21 or later
- Docker and Docker Compose (for local development)
- PostgreSQL 14 or later
- Redis 6 or later
- Git
- Gradle 8.5+ (or use the provided Gradle wrapper)

## Getting Started

### 1. Clone the Repository

```bash
# Clone the repository
git clone https://github.com/your-organization/tiny-url-service.git

# Navigate to the project directory
cd tiny-url-service
```

### 2. Set Up the Environment

```bash
# Create the logs directory
mkdir -p logs
chmod 755 logs
```

### 3. Database Setup

#### Creating the PostgreSQL Database

1. Connect to PostgreSQL:
   ```bash
   psql -U postgres
   ```

2. Create the 'tinyurl' database:
   ```sql
   CREATE DATABASE tinyurl;
   ```

3. Connect to the newly created database:
   ```bash
   \c tinyurl
   ```

4. Create the necessary tables:
   ```sql
   -- Create the tiny_urls table
   CREATE TABLE tiny_urls (
       id BIGSERIAL PRIMARY KEY,
       original_url VARCHAR(2048) NOT NULL,
       short_code VARCHAR(10) NOT NULL UNIQUE,
       expiration_time TIMESTAMP,
       one_time_use BOOLEAN NOT NULL DEFAULT FALSE,
       usage_count INT NOT NULL DEFAULT 0,
       max_usage INT NOT NULL DEFAULT 0,
       max_attempts INT NOT NULL DEFAULT 0,
       attempt_count INT NOT NULL DEFAULT 0,
       created_at TIMESTAMP NOT NULL,
       active BOOLEAN NOT NULL DEFAULT TRUE
   );

   -- Create indexes for better performance
   CREATE INDEX idx_tiny_urls_short_code ON tiny_urls(short_code);
   CREATE INDEX idx_tiny_urls_expiration ON tiny_urls(expiration_time) WHERE expiration_time IS NOT NULL;
   ```

#### Alternative: Using the provided SQL script

A `db-init.sql` script is included in the `src/main/resources/db/init` directory. You can run it with:

```bash
psql -U postgres -f src/main/resources/db/init/db-init.sql
```

Content of `db-init.sql`:
```sql
-- Create the tinyurl database
CREATE DATABASE tinyurl;

-- Connect to the tinyurl database
\c tinyurl;

-- Create the tiny_urls table
CREATE TABLE tiny_urls (
    id BIGSERIAL PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    short_code VARCHAR(10) NOT NULL UNIQUE,
    expiration_time TIMESTAMP,
    one_time_use BOOLEAN NOT NULL DEFAULT FALSE,
    usage_count INT NOT NULL DEFAULT 0,
    max_usage INT NOT NULL DEFAULT 0,
    max_attempts INT NOT NULL DEFAULT 0,
    attempt_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create indexes for better performance
CREATE INDEX idx_tiny_urls_short_code ON tiny_urls(short_code);
CREATE INDEX idx_tiny_urls_expiration ON tiny_urls(expiration_time) WHERE expiration_time IS NOT NULL;

-- Create a user for the application (optional)
CREATE USER tinyurl_app WITH ENCRYPTED PASSWORD 'tinyurl_password';

-- Grant necessary privileges
GRANT ALL PRIVILEGES ON DATABASE tinyurl TO tinyurl_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tinyurl_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tinyurl_app;
```

### 4. Docker Setup for Services (Optional)

You can use Docker to set up the required database and cache services:

```bash
# Start PostgreSQL
docker run --name tinyurl-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=tinyurl -p 5432:5432 -d postgres:14

# Initialize the database with the schema
docker cp src/main/resources/db/init/db-init.sql tinyurl-postgres:/tmp/
docker exec -it tinyurl-postgres psql -U postgres -f /tmp/db-init.sql

# Start Redis
docker run --name tinyurl-redis -p 6379:6379 -d redis:6
```

### 5. Docker Compose (Recommended)

The easiest way to start all required services is using Docker Compose:

```bash
# Start all services
docker-compose up -d
```

This will start PostgreSQL, Redis, and other required services as defined in the docker-compose.yml file.

### 6. Build the Project

```bash
# Build with Gradle (use gradlew if you don't have Gradle installed)
./gradlew build
```

This will download all dependencies, compile the code, and run tests.

### 7. Run the Application

#### Development Mode (with Hot Reload)

```bash
# Run in development mode
./gradlew quarkusDev
```

The application will be available at http://localhost:8080 with hot reload enabled.

#### Production Mode

```bash
# Run the JAR file
java -jar build/quarkus-app/quarkus-run.jar
```

Or with a specific profile:

```bash
# Run with production profile
java -Dquarkus.profile=prod -jar build/quarkus-app/quarkus-run.jar
```

### 8. Verify Installation

1. Check if the application is running:
   ```bash
   curl http://localhost:8080/health
   ```

2. Access the Swagger UI for API documentation:
   ```
   http://localhost:8080/swagger-ui
   ```

3. Create a test tiny URL:
   ```bash
   curl -X POST http://localhost:8080/api/urls \
     -H "Content-Type: application/json" \
     -d '{"originalUrl":"https://example.com", "maxUsage": 5}'
   ```

## API Documentation

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui
```

This provides interactive documentation of all available API endpoints.

## Key API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/urls` | POST | Create a new tiny URL |
| `/api/urls/{shortCode}` | GET | Redirect to the original URL |
| `/api/urls/info/{shortCode}` | GET | Get information about a tiny URL |
| `/api/urls/{shortCode}` | DELETE | Deactivate a tiny URL |
| `/api/urls/{shortCode}/expiration` | PUT | Update expiration time |
| `/api/urls/{shortCode}/max-usage` | PUT | Update maximum usage limit |
| `/api/urls/{shortCode}/max-attempts` | PUT | Update maximum attempts limit |

## Configuration

The application can be configured through the `application.properties` file or environment variables. Key configuration properties include:

```properties
# Database configuration
quarkus.datasource.reactive.url=postgresql://localhost:5432/tinyurl
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres

# Redis configuration
quarkus.redis.hosts=redis://localhost:6379

# Application specific configuration
tiny.url.base.url=http://localhost:8080/t/
tiny.url.code.length=6
```

## Monitoring

Health and metrics endpoints are available at:

- Health: `http://localhost:8080/health`
- Metrics: `http://localhost:8080/metrics`

## Logging

Logs are written to the `logs` directory:

- Application logs: `logs/application.log`
- Access logs: `logs/access.log`

## Database Migration

Database migrations are managed with Liquibase and run automatically at startup. Migration scripts are located in `src/main/resources/db/changelog/`.

## Building for Production

To build a production-ready application:

```bash
# Build a native executable
./gradlew build -Dquarkus.package.type=native
```

This creates a native executable for improved startup time and reduced memory usage.

## Troubleshooting

### Common Issues

1. **Database connection failure**:
    - Verify PostgreSQL is running: `docker ps | grep postgres`
    - Check connection details in application.properties
    - Ensure database and tables exist: `psql -U postgres -d tinyurl -c "\dt"`

2. **Redis connection failure**:
    - Verify Redis is running: `docker ps | grep redis`
    - Check Redis connection in application.properties

3. **Application won't start**:
    - Check logs in the logs directory
    - Verify Java version: `java -version`
    - Ensure ports are available: `netstat -tulpn | grep 8080`

### Getting Help

If you encounter issues not covered here:
1. Check the application logs
2. Review the Quarkus documentation
3. File an issue in the GitHub repository

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Contact

For any questions or support, please contact:
- Email: support@example.com
- Website: https://example.com/support