# define base docker image
FROM openjdk:18
LABEL maintainer="MostafaWahied"
ADD target/mastermind-webapp-0.0.1-SNAPSHOT.jar mastermind-webapp-docker.jar
ENTRYPOINT ["java", "-jar", "mastermind-webapp-docker.jar"]