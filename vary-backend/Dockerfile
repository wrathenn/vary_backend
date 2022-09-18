FROM maven:3.6-jdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app/pom.xml
WORKDIR /usr/src/app
RUN mvn -T 1C package -Dmaven.test.skip=true
#ENTRYPOINT ["tail", "-f", "/dev/null"]

# creates runtime
FROM adoptopenjdk/openjdk11:alpine-jre
ENV LANG en_US.UTF-8
COPY --from=build /usr/src/app/target/*.jar /vary-backend.jar
CMD java -jar -Dspring.profiles.active=prod /vary-backend.jar
