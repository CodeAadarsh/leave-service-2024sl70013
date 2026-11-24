FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/leave-service.jar leave-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "leave-service.jar"]
