# Healthcare Management System

Enterprise-grade healthcare management platform with multi-tenant support, comprehensive CRUD operations, JWT-based authentication, and full audit capabilities.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Multi-Tenant Configuration](#multi-tenant-configuration)
- [Database Migration](#database-migration)
- [Testing](#testing)
- [Containerization](#containerization)
- [ELK Stack Integration](#elk-stack-integration)
- [Deployment](#deployment)
- [Security](#security)

## Features

### Core Healthcare Management
- **User Management**: Complete user profile management with role-based access
- **Role & Permission System**: Granular permission management with RBAC
- **Patient Management**: Comprehensive patient records including:
  - Demographics and identifiers (MRN)
  - Vital signs and medical history
  - Primary, secondary, and referral physicians
- **Visit Management**: Complete visit tracking and documentation
- **Clinical Data**: Diagnosis, allergies, and medication management
- **Document Management**: File upload/download supporting images, PDFs, DOCs at patient and visit levels
- **User-Patient Assignments**: Healthcare provider assignments

### Security
- JWT-based authentication with access and refresh tokens
- Role-based access control (RBAC)
- Permission-based endpoint security
- Secure password encoding

### Multi-Tenancy
- Host-based (subdomain) tenant resolution
- Header-based tenant resolution
- Automatic tenant context management
- Profile-based configuration management
- In-memory configuration support

### Data Management
- JPA auditing with automatic audit field population
- Flyway database migration
- Comprehensive audit trail API

### Quality Assurance
- 90%+ unit test coverage
- Integration tests for critical flows
- JaCoCo coverage reporting

### Observability
- ELK stack integration for log management
- Application health checks
- Performance monitoring with Actuator

## Architecture

The project consists of two main modules:

1. **multi-tenant-lib**: Reusable JAR library for multi-tenant applications
2. **healthcare-system**: Main healthcare management application

### Technology Stack
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **PostgreSQL** (production) / H2 (testing)
- **Flyway** for database migrations
- **OpenAPI/Swagger** for API documentation
- **Maven** for build management
- **JUnit 5** and **Mockito** for testing
- **JaCoCo** for code coverage

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (for production)
- Docker/Podman (for containerized deployment)
- 4GB+ RAM recommended

## Quick Start

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PIP
   ```

2. **Set up PostgreSQL database**
   ```bash
   createdb healthcare
   ```

3. **Configure application properties**
   Edit `healthcare-system/src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/healthcare
       username: your_username
       password: your_password
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   cd healthcare-system
   mvn spring-boot:run
   ```

6. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

### Default Credentials

After running migrations, default admin user is created:
- **Username**: `admin`
- **Password**: `admin123`

**⚠️ IMPORTANT**: Change the default password in production!

## Building the Project

### Build All Modules
```bash
mvn clean install
```

### Build with Tests
```bash
mvn clean verify
```

### Build Without Tests
```bash
mvn clean install -DskipTests
```

### Generate Coverage Report
```bash
mvn clean verify
# Coverage report available at: healthcare-system/target/site/jacoco/index.html
```

## Running the Application

### Using Maven
```bash
cd healthcare-system
mvn spring-boot:run
```

### Using JAR
```bash
cd healthcare-system
mvn clean package
java -jar target/healthcare-system-1.0.0.jar
```

### Using Docker/Podman
```bash
# Build image
docker build -t healthcare-system:1.0.0 .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/healthcare \
  -e SPRING_DATASOURCE_USERNAME=healthcare_app \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  healthcare-system:1.0.0
```

### Using Docker Compose (Full Stack)
```bash
docker-compose up -d
```

This starts:
- PostgreSQL database
- Healthcare API
- Elasticsearch
- Logstash
- Kibana

Access Kibana at: http://localhost:5601

## API Documentation

### Swagger UI
Once the application is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### API Endpoints

#### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - Logout

#### Users
- `GET /api/v1/users` - List users (paginated)
- `GET /api/v1/users/{id}` - Get user by ID
- `POST /api/v1/users` - Create user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

#### Patients
- `GET /api/v1/patients` - List patients (paginated)
- `GET /api/v1/patients/{id}` - Get patient by ID
- `POST /api/v1/patients` - Create patient
- `PUT /api/v1/patients/{id}` - Update patient
- `DELETE /api/v1/patients/{id}` - Delete patient

#### Roles & Permissions
- `GET /api/v1/roles` - List roles
- `POST /api/v1/roles` - Create role
- `GET /api/v1/permissions` - List permissions
- `POST /api/v1/permissions` - Create permission

#### Visits
- `GET /api/v1/visits` - List visits
- `POST /api/v1/visits` - Create visit
- `GET /api/v1/visits/patient/{patientId}` - Get visits by patient

#### Audit
- `GET /api/v1/audit/{entityType}/{entityId}` - Get audit info for entity
- `GET /api/v1/audit/entity/{entityType}` - Get audit info by entity type
- `GET /api/v1/audit/user/{username}` - Get audit info by user

### Authentication

All endpoints (except `/api/v1/auth/*`) require JWT authentication.

Include the token in the Authorization header:
```
Authorization: Bearer <access_token>
```

## Multi-Tenant Configuration

### Configuration Structure

The multi-tenant library supports profile-based configuration:

```yaml
multitenant:
  enabled: true
  resolution:
    host-enabled: true
    header-enabled: true
    header-name: X-Tenant-Id
  store:
    type: PROFILE
    defaults:
      connection-string: jdbc:postgresql://localhost:5432/default_db
    tenants:
      - id: unique-id-0ff4daf
        identifier: tenant-1
        name: Tenant 1 Company Name
        connection-string: jdbc:postgresql://localhost:5432/tenant1_db
        properties:
          customProperty: VIP Customer
      - id: unique-id-ao41n44
        identifier: tenant-2
        name: Tenant 2 Company Name
        connection-string: jdbc:postgresql://localhost:5432/tenant2_db
```

### Tenant Resolution Strategies

1. **Host-based**: Resolves tenant from subdomain (e.g., `tenant1.example.com`)
2. **Header-based**: Resolves tenant from HTTP header (default: `X-Tenant-Id`)

### Using Multi-Tenant Library

The library is automatically configured via Spring Boot auto-configuration. Simply include the dependency:

```xml
<dependency>
    <groupId>com.acme.health</groupId>
    <artifactId>multi-tenant-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Database Migration

Flyway is configured to automatically run migrations on application startup.

### Migration Files
Migrations are located in: `healthcare-system/src/main/resources/db/migration/`

### Manual Migration
```bash
mvn flyway:migrate -Dflyway.configFiles=healthcare-system/src/main/resources/application.yml
```

### Rollback
Flyway doesn't support automatic rollback. To rollback:
1. Manually revert database changes
2. Or restore from backup

## Testing

### Run All Tests
```bash
mvn test
```

### Run Tests with Coverage
```bash
mvn clean verify
# View report: healthcare-system/target/site/jacoco/index.html
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserServiceImplTest
```

### Integration Tests
Integration tests require a running database. Use the test profile:
```bash
mvn test -Dspring.profiles.active=test
```

### Coverage Requirements
- Minimum coverage: 90%
- Coverage is enforced in the `verify` phase
- Build will fail if coverage is below threshold

## Containerization

### Dockerfile
The project includes a multi-stage Dockerfile for optimized image size.

### Docker Compose
Full stack deployment including:
- Application
- PostgreSQL
- ELK Stack (Elasticsearch, Logstash, Kibana)

### Build and Run
```bash
# Build image
docker build -t healthcare-system:1.0.0 .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f healthcare-api

# Stop services
docker-compose down
```

### Health Checks
The application includes health checks:
- Liveness: `/actuator/health/liveness`
- Readiness: `/actuator/health/readiness`

## ELK Stack Integration

### Configuration

Logs are automatically shipped to Logstash via TCP on port 5000.

### Logstash Pipeline
Located at: `logstash/pipeline/logstash.conf`

### Access Kibana
1. Start services: `docker-compose up -d`
2. Access Kibana: http://localhost:5601
3. Create index pattern: `healthcare-logs-*`
4. Explore logs in Discover

### Log Format
Logs are in JSON format with fields:
- `@timestamp`
- `level`
- `logger`
- `message`
- `tenant` (if multi-tenant context available)

## Deployment

### Production Checklist

- [ ] Change default admin password
- [ ] Set strong JWT secret (`security.jwt.secret`)
- [ ] Configure production database
- [ ] Set up SSL/TLS
- [ ] Configure firewall rules
- [ ] Set up backup strategy
- [ ] Configure monitoring and alerting
- [ ] Review and adjust logging levels
- [ ] Set up document storage with proper permissions
- [ ] Configure multi-tenant settings

### Environment Variables

Key environment variables:
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret (minimum 256 bits)
- `DOCUMENT_ROOT` - Document storage root path
- `LOGSTASH_HOST` - Logstash host
- `LOGSTASH_PORT` - Logstash port

### Podman Deployment

```bash
# Build with Podman
podman build -t healthcare-system:1.0.0 .

# Run container
podman run -d \
  --name healthcare-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/healthcare \
  healthcare-system:1.0.0

# View logs
podman logs -f healthcare-api
```

## Security

### JWT Configuration
- Access tokens: 30 minutes TTL
- Refresh tokens: 12 hours TTL
- Secret key: Must be at least 256 bits

### Role-Based Access Control
Default roles:
- **ADMIN**: Full system access
- **PHYSICIAN**: Patient care and documentation
- **NURSE**: Limited patient access, visit management
- **THERAPIST**: Specialized care access

### Permissions
Permissions follow the pattern: `{RESOURCE}_{ACTION}`
- Resources: USER, PATIENT, VISIT, DIAGNOSIS, ALLERGY, MEDICATION, DOCUMENT, ROLE, AUDIT
- Actions: CREATE, READ, UPDATE, DELETE

### Best Practices
1. Always use HTTPS in production
2. Rotate JWT secrets regularly
3. Implement rate limiting
4. Use strong password policies
5. Regular security audits
6. Keep dependencies updated

## Troubleshooting

### Application Won't Start
1. Check database connectivity
2. Verify Flyway migrations completed
3. Check port 8080 is available
4. Review application logs

### Database Connection Issues
1. Verify PostgreSQL is running
2. Check credentials in `application.yml`
3. Ensure database exists
4. Check network connectivity

### Multi-Tenant Issues
1. Verify tenant configuration
2. Check tenant resolution strategy
3. Review tenant context in logs
4. Ensure tenant database exists

### Test Failures
1. Ensure H2 database is available for tests
2. Check test profile configuration
3. Review test logs for specific failures
4. Verify all dependencies are resolved

## Contributing

1. Follow Java coding standards
2. Maintain 90%+ test coverage
3. Update documentation
4. Run all tests before committing
5. Follow Git commit message conventions

## License

Proprietary - All rights reserved

## Support

For support, contact: support@healthcare.com

---

**Version**: 1.0.0  
**Last Updated**: 2024











