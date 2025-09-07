# ===== build =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copy parent pom.xml first
COPY pom.xml ./

# Copy all module pom.xml files
COPY common/pom.xml ./common/
COPY pojo/pom.xml ./pojo/
COPY server/pom.xml ./server/

# Download dependencies (this layer will be cached if pom.xml files don't change)
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy source code
COPY common/src ./common/src
COPY pojo/src ./pojo/src
COPY server/src ./server/src

# Build the application
RUN mvn -B -q -DskipTests -pl server -am package

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
