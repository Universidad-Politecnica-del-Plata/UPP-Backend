# Use Maven to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (improves caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src/ src/

# Package the application (creates the JAR file)
RUN mvn clean package -DskipTests

# Use a lightweight JDK for the final image
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy only the JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
