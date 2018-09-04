FROM maven:3-jdk-8-slim AS build

WORKDIR /usr/src/app
ADD . /usr/src/app

RUN mvn clean package

FROM openjdk:10-jre-slim

# The name of the application's fat JAR
ARG APPLICATION=service

WORKDIR /usr/src/app/target

COPY --from=build /usr/src/app/timer-${APPLICATION}/target/service-timer-${APPLICATION}.jar /usr/src/app/target/application.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/target/application.jar"]
