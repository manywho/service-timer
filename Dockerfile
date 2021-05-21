FROM maven:3-jdk-8-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY pom.xml pom.xml
COPY timer-service timer-service
COPY timer-common timer-common
COPY timer-worker timer-worker
RUN mvn clean package -DskipTests=true

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
