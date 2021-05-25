FROM maven:3-jdk-8-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/timer-service

ADD ../ /usr/src/app

EXPOSE 8080

CMD ["java", "-Xmx300m", "-jar", "/usr/src/app/timer-service/target/timer-service-1.0-SNAPSHOT.jar"]
