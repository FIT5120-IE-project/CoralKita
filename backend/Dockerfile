# ===== build =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copy the entire project structure
COPY . .

# Build the application
RUN mvn -B -q -DskipTests clean package

# ===== run =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar file
COPY --from=build /build/server/target/server-1.0-SNAPSHOT.jar /app/app.jar

# Set JVM options for container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
