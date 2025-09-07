# ===== build =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# 只先拷 poms 以便 go-offline
COPY pom.xml ./pom.xml
COPY common/pom.xml common/pom.xml
COPY pojo/pom.xml pojo/pom.xml
COPY server/pom.xml server/pom.xml

RUN mvn -B -q -DskipTests dependency:go-offline

# 再拷源码
COPY common/src common/src
COPY pojo/src pojo/src
COPY server/src server/src

# 只构建 server（-pl 指定模块，-am 自动构建依赖模块）
RUN mvn -B -q -DskipTests -pl server -am package

# ===== run =====
FROM eclipse-temurin:21-jre
WORKDIR /app
# 如果不是 SNAPSHOT，请把通配符改成你的真实文件名
COPY --from=build /build/server/target/*-SNAPSHOT.jar /app/app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
EXPOSE 8080
CMD ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
