#FROM openjdk:11-jdk-slim AS build
FROM gradle:6.2-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
COPY . /home/gradle/src
WORKDIR /home/gradle/src

#RUN ./gradlew build --no-daemon
#RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
ENV CHROME_VERSION="80.0.3987.132-1"

EXPOSE 9000

RUN mkdir /app

RUN apt-get update && apt-get install -y wget
RUN wget https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_${CHROME_VERSION}_amd64.deb
RUN apt-get

RUN dpkg -i google-chrome*.deb
RUN apt-get install -f

COPY --from=build /home/gradle/src/build/libs/*.jar /app/learngine.jar

ENTRYPOINT ["java", "-jar", "/app/learngine.jar"]