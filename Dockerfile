FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app


COPY --from=build /app/target/Whisper-0.0.1-SNAPSHOT.jar app.jar

EXPOSE ${AUTH_SERVICE_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
