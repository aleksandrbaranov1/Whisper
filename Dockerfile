FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем pom.xml и исходники
COPY pom.xml .
COPY src ./src

# Скачиваем зависимости
RUN mvn -B -q -DskipTests -Dstyle.color=never --no-transfer-progress dependency:go-offline

# Сборка
RUN mvn -B -q -DskipTests clean package

# -------------------------
FROM gcr.io/distroless/java17-debian12:nonroot
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50 -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC"

# Копируем jar
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
