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

RUN mkdir /app

# Install chrome
RUN apt-get update && apt-get install -y \
    wget fonts-liberation libappindicator3-1 libasound2 libnspr4 libnss3 libx11-xcb1 libxss1 xdg-utils xvfb \
    && wget https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_${CHROME_VERSION}_amd64.deb \
    && dpkg -i google-chrome*.deb \
    && apt-get -fy install
RUN apt-get install -y xvfb

USER learngine

COPY drivers/chromedriver_${CHROME_DRIVER_VERSION} /app/drivers/chromedriver
COPY --from=build /home/gradle/src/build/libs/*.jar /app/learngine.jar

ENTRYPOINT ["java", "-Dwebdriver.chrome.driver=/app/drivers/chromedriver", "-jar", "/app/learngine.jar"]