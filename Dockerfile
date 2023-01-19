#
# Build stage
#
FROM maven:3.8.7 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:18
COPY --from=build /target/mastermind-webapp-0.0.1-SNAPSHOT.jar mastermind-webapp-docker.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mastermind-webapp-docker.jar"]

#LABEL maintainer="MostafaWahied"
#ADD target/mastermind-webapp-0.0.1-SNAPSHOT.jar mastermind-webapp-docker.jar
