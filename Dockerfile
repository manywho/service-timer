FROM maven:alpine AS build

FROM maven:slim AS build
WORKDIR /usr/src/app

COPY pom.xml pom.xml
RUN mvn dependency:resolve
COPY src src
RUN mvn clean package

FROM openjdk:jre-alpine

FROM openjdk:8-jre-slim
EXPOSE 8080


COPY --from=build /usr/src/app/target/timer-service.jar /usr/src/app/target/timer-service.jar
CMD java -Xmx300m -jar /usr/src/app/target/timer-service.jar
COPY --from=build /usr/src/app/target/timer-service.jar /usr/src/app/target/timer-service.jar

COPY --from=build /usr/src/app/target/timer-worker.jar /usr/src/app/target/timer-worker.jar
CMD java -Xmx300m -jar /usr/src/app/target/timer-worker.jar
COPY --from=build /usr/src/app/target/timer-worker.jar /usr/src/app/target/timer-worker.jar

COPY --from=build /usr/src/app/target/timer-common.jar /usr/src/app/target/timer-common.jar
CMD java -Xmx300m -jar /usr/src/app/target/timer-common.jar
COPY --from=build /usr/src/app/target/timer-common.jar /usr/src/app/target/timer-common.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/target/timer-service.jar"]