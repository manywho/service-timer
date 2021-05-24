FROM maven:3-jdk-8-alpine AS build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY pom.xml pom.xml
COPY timer-service timer-service
COPY timer-common timer-common
COPY timer-worker timer-worker
RUN mvn clean package -DskipTests=true
RUN mvn install -DskipTests=true
FROM openjdk:8-jre-slim

EXPOSE 8080

COPY --from=build /usr/src/app/timer-service/target/timer-service-1.0-SNAPSHOT.jar /usr/src/app/target/timer-service-1.0-SNAPSHOT.jar
CMD java -Xmx300m -jar /usr/src/app/timer-service/target/timer-service-1.0-SNAPSHOT.jar

COPY --from=build /usr/src/app/timer-worker/target/timer-worker-1.0-SNAPSHOT.jar /usr/src/app/target/timer-worker-1.0-SNAPSHOT.jar
CMD java -Xmx300m -jar /usr/src/app/timer-worker/target/timer-worker-1.0-SNAPSHOT.jar

COPY --from=build /usr/src/app/timer-common/target/timer-common-1.0-SNAPSHOT.jar /usr/src/app/target/timer-common-1.0-SNAPSHOT.jar
CMD java -Xmx300m -jar /usr/src/app/timer-common/target/timer-common-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/target/timer-service-1.0-SNAPSHOT.jar"]