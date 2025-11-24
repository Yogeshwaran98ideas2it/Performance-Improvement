FROM docker.io/library/maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY audit-service/pom.xml audit-service/pom.xml
COPY common-dtos/pom.xml common-dtos/pom.xml
COPY multi-tenant-lib/pom.xml multi-tenant-lib/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml
COPY discovery-server/pom.xml discovery-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY healthcare-system/pom.xml healthcare-system/pom.xml

RUN rm -rf /root/.m2/repository && \
    mvn -pl audit-service -am dependency:go-offline -P '!contract-tests' -Dcontract.tests.skip=true -B

COPY . .

RUN mvn -pl audit-service -am clean package -DskipTests -P '!contract-tests' -Dcontract.tests.skip=true -B

FROM docker.io/library/eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /workspace/audit-service/target/*.jar app.jar

USER appuser

EXPOSE 8085

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8085/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

