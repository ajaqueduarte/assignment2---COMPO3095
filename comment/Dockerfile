# -------------------------------
# Docker File for user-service
# -------------------------------

# ------------
# BUILD stage
# ------------

# Base image
FROM gradle:8-jdk17 AS builder

# Copy the application files from the host machine to the image filesystem
COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory within the image for executing future commands
WORKDIR /home/gradle/src

# Build the application with Gradle within the container
RUN gradle build -x test

# ------------
# PACKAGE stage
# ------------

# Base Image
FROM openjdk:17-jdk

# Create a directory for the application
RUN mkdir /app

# Copy the JAR file from the builder stage to the /app directory in the final image
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/comment.jar

# Set environment variables for MongoDB connection (if needed)
ENV MONGO_DB_USERNAME=mongoadmin \
    MONGO_DB_PWD=password

# Expose the application port
EXPOSE 8082

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/comment.jar"]
