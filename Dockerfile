#FROM openjdk:11-jdk-slim AS build
FROM gradle:6.2-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
COPY . /home/gradle/src
WORKDIR /home/gradle/src

#RUN ./gradlew build --no-daemon
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
ENV CHROME_VERSION="80.0.3987.132-1"
ENV CHROME_DRIVER_VERSION="80"

EXPOSE 9000

RUN apt-get update -y

RUN useradd -ms /bin/bash learngine
USER learngine

COPY --from=build /home/gradle/src/build/libs/*.jar /home/learngine/learngine.jar

ENTRYPOINT ["java", "-Dwebdriver.chrome.driver=/app/drivers/chromedriver", "-Dspring.profiles.active=dev", "-jar", "/home/learngine/learngine.jar"]