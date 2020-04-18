FROM openjdk:11-jdk-slim AS build

RUN useradd -ms /bin/bash gradle

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN ./gradlew build --no-daemon --exclude-task check

FROM openjdk:11-jre-slim
ENV CHROME_VERSION="80.0.3987.132-1"
ENV CHROME_DRIVER_VERSION="80"

EXPOSE 9000

RUN apt-get update -y

RUN useradd -ms /bin/bash learngine
USER learngine

COPY --from=build /home/gradle/src/build/libs/*.jar /home/learngine/learngine.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/home/learngine/learngine.jar"]