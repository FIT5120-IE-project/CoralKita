# ===== build =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# ===== run =====
FROM eclipse-temurin:21-jre
WORKDIR /app
# 如果产物不是 *-SNAPSHOT.jar，改成你的实际 JAR 名
COPY --from=build /build/target/*-SNAPSHOT.jar /app/app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]