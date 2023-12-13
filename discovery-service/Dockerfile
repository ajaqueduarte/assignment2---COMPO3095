FROM gradle:8-jdk17 AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build -x test

# ===============================

FROM openjdk:17
RUN mkdir /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/discovery-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]
