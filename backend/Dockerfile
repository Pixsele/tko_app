FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/backend-0.1.jar /app/backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "backend.jar"]