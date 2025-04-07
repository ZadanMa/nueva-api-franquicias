# Stage 1: Build the application
FROM eclipse-temurin:22-jdk-alpine AS builder
WORKDIR /app

# Remove any existing Gradle setup
RUN rm -rf /opt/gradle /usr/bin/gradle

# Download and install the latest stable Gradle version (8.13)
RUN wget -q https://services.gradle.org/distributions/gradle-8.13-bin.zip -P /tmp
RUN unzip -q -d /opt/gradle /tmp/gradle-8.13-bin.zip
RUN ln -s /opt/gradle/gradle-8.13/bin/gradle /usr/bin/gradle

# Verify Gradle installation
RUN gradle --version

# Copy build files AFTER ensuring Gradle is set up
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application
RUN gradle build --no-daemon -x test

# Stage 2: Create the runtime image
FROM eclipse-temurin:22-jre-alpine AS runtime
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your application runs on (if applicable)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]