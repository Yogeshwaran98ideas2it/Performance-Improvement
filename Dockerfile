# Stage 1: Build the application
FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copy root descriptor and module POMs to resolve dependencies up front
COPY pom.xml .
COPY common-dtos/pom.xml common-dtos/pom.xml
COPY multi-tenant-lib/pom.xml multi-tenant-lib/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml
COPY audit-service/pom.xml audit-service/pom.xml
COPY discovery-server/pom.xml discovery-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY healthcare-system/pom.xml healthcare-system/pom.xml

# Pre-fetch dependencies for faster, reliable builds in air-gapped environments
RUN rm -rf /root/.m2/repository && \
    mvn -pl healthcare-system -am dependency:go-offline -P '!contract-tests' -B

# Copy the remaining project sources
COPY . .

# Build the healthcare-system module (other modules are pulled in transitively)
RUN mvn -pl healthcare-system -am clean package -DskipTests -P '!contract-tests' -B

# Stage 2: Create runtime image
FROM docker.io/library/eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built JAR from build stage
COPY --from=build /workspace/healthcare-system/target/*.jar app.jar

# Create document storage directory with appropriate ownership and permissions
RUN install -d -m 755 -o appuser -g appgroup /var/healthcare/documents

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]


