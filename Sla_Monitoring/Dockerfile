# Use Java 21 as base image
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
# Copy the jar file from target folder
COPY target/*.jar app.jar
# Command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]