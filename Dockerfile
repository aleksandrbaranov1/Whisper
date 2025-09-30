FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Кэшируем зависимости: сначала только pom.xml
COPY pom.xml .
RUN mvn -B -q -e -DskipTests -Dstyle.color=never --no-transfer-progress dependency:go-offline

# Затем копируем исходники и собираем
COPY src ./src
RUN mvn -B -q -DskipTests clean package && \
    cp target/*-SNAPSHOT.jar app.jar
FROM gcr.io/distroless/java17-debian12:nonroot
WORKDIR /app

# Безопасные и разумные JVM дефолты
ENV JAVA_TOOL_OPTIONS "-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50 -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -Duser.timezone=UTC"

# Копируем билд-артефакт
COPY --from=build /app/app.jar /app/app.jar

# Порт Spring Boot по умолчанию
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
