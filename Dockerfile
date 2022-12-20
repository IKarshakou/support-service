FROM openjdk:21-oraclelinux8

RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app

ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} /usr/src/app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/src/app/app.jar"]
