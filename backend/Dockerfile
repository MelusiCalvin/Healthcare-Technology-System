FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY backend/pom.xml .
COPY backend/ubuntu-health-shared-kernel/pom.xml ubuntu-health-shared-kernel/pom.xml
COPY backend/ubuntu-health-contracts/pom.xml ubuntu-health-contracts/pom.xml
COPY backend/modules modules
COPY backend/ubuntu-health-bootstrap/pom.xml ubuntu-health-bootstrap/pom.xml
RUN mvn -B -pl ubuntu-health-bootstrap -am dependency:go-offline

COPY database /workspace/database
COPY backend/ubuntu-health-shared-kernel/src ubuntu-health-shared-kernel/src
COPY backend/ubuntu-health-contracts/src ubuntu-health-contracts/src
COPY backend/ubuntu-health-bootstrap/src ubuntu-health-bootstrap/src
RUN mvn -B -pl ubuntu-health-bootstrap -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN apk add --no-cache wget \
	&& addgroup -S ubuntuhealth \
	&& adduser -S ubuntuhealth -G ubuntuhealth
COPY --from=build /workspace/ubuntu-health-bootstrap/target/ubuntu-health-bootstrap-*.jar /app/app.jar
USER ubuntuhealth
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
