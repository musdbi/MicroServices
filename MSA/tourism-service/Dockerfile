FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar tourism-service.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "tourism-service.jar"]