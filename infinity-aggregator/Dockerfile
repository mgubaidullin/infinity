FROM quay.io/quarkus/centos-quarkus-maven:20.1.0-java11 AS build
COPY . /usr/src/app
USER root
RUN chown -R quarkus /usr/src/app
USER quarkus
RUN mvn -f /usr/src/app/pom.xml clean package

## Stage 2 : create the docker final image for rest app
FROM gcr.io/distroless/java:11 as rest
COPY --from=build /usr/src/app/target/*-runner.jar /app/app.jar
WORKDIR /app
CMD ["app.jar"]