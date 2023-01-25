############ trying ci/cd for aws ###########
#FROM openjdk:18
## ENV PORT=8080
#EXPOSE 8080
#ADD target/mastermind-webapp-0.0.1-SNAPSHOT.jar mastermind-webapp-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java", "-jar", "mastermind-webapp-0.0.1-SNAPSHOT.jar"]
#
#
#
#
### end of trying ##


############### this version was working fine with render! ###############
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

# to use the docker file, first we need to build the docker image
# run in terminal 'docker build -t spring-boot-render .'
# then we can run the docker image
# run in terminal 'docker run -p 8080:8080 spring-boot-render'