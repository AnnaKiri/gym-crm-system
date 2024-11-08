FROM openjdk:21-jdk-slim

WORKDIR /app

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

EXPOSE 80

ENV SPRING_PROFILES_ACTIVE=dev
ENV CRM_SYSTEM_ROOT=/var

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]
