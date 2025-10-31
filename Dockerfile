# ===== Build Stage =====
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Then copy the source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ===== Runtime Stage =====
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Render sets PORT as env variable (for internal routing)
ENV PORT=8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
