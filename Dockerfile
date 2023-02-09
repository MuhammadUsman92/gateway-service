FROM openjdk:17-alpine

EXPOSE 8080

ARG JAR_FILE=target/gateway-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT exec java -jar /app.jar