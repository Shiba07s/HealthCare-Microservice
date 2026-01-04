FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/patient-service.jar patient-service.jar
EXPOSE 3031
ENTRYPOINT ["java","-jar","patient-service.jar"]