FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar travel-service.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "travel-service.jar"]