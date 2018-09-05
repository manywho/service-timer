FROM maven:3-jdk-8-slim AS build

WORKDIR /usr/src/app
ADD . /usr/src/app

RUN mvn clean package

FROM openjdk:8-jre-slim

WORKDIR /usr/src/app/target

COPY --from=build /usr/src/app/target/service-timer.jar /usr/src/app/target/service-timer.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/target/service-timer.jar"]
