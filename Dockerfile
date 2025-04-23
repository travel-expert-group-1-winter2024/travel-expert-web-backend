# Use OpenJDK as base image
FROM openjdk:21-jdk-slim


# Set environment variable
ENV JAVA_OPTS=""

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/travel-expert-web-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional, if your app listens on 8080)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]