# Stage 1: Build the Spring Boot application
# Use Maven with JDK 17 to compile the application
FROM maven:3.9.2-eclipse-temurin-17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the pom.xml first to leverage Docker caching
COPY pom.xml .

# Download dependencies (this layer is cached if pom.xml doesn't change)
RUN mvn dependency:go-offline

# Copy the source code
COPY src src

# Build the Spring Boot application (skip tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:17-jre-focal

# Set working directory inside the container
WORKDIR /app

# Copy the executable JAR from the builder stage
COPY --from=builder /app/target/conersion-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 9090

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
