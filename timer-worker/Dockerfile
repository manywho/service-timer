FROM maven:3-jdk-8-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/timer-worker

ADD ../ /usr/src/app

RUN mvn install

CMD ["java", "-Xmx250m", "-jar", "/usr/src/app/timer-worker/target/timer-worker-1.0-SNAPSHOT.jar"]
