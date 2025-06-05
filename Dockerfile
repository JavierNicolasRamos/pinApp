ARG APPLICATION_NAME=pinApp

FROM openjdk:17-jdk-alpine as development
ARG APPLICATION_NAME
WORKDIR /app/$APPLICATION_NAME

COPY ./pom.xml /app
COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .
COPY src/main/resources/usersDataSeed.json /app/$APPLICATION_NAME/src/main/resources/
COPY src/main/resources/clientsDataSeed.json /app/$APPLICATION_NAME/src/main/resources/

# Install dos2unix and convert mvnw to Unix format
RUN apk update && apk add dos2unix
RUN dos2unix mvnw

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine
ARG APPLICATION_NAME

#WORKDIR /app
#RUN mkdir "./logs"

ARG TARGET_FOLDER=./app/$APPLICATION_NAME/target
COPY --from=development $TARGET_FOLDER/challenge-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8001
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java","-jar","challenge-0.0.1-SNAPSHOT.jar"]