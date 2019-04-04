# This Docker file uses the builder pattern with two stages: Development and production
# For more information see: https://medium.com/@kaperys/create-lean-docker-images-using-the-builder-pattern-37fe2b5d97d4

# Define the development environment
FROM openjdk:11-jdk-slim as development-environment

# Copy the build system related files
COPY gradlew gradlew.bat build.gradle settings.gradle /workspace/
COPY config /workspace/config/
COPY gradle /workspace/gradle/

# Download the build dependencies
RUN cd /workspace && ./gradlew --no-daemon downloadDependencies

# Copy the source code to improve caching (Improves caching)
COPY src ./workspace/src

# Copy and build the project
RUN cd /workspace && ./gradlew --no-daemon flywayMigrate -i && ./gradlew --no-daemon --info clean build

# Define the production environment
FROM openjdk:11-jre-slim

# Copy the application
COPY --from=development-environment /workspace/build/libs/fhnwwodss.jar /opt/app/app.jar

# Start the application
CMD ["java", "-XX:+UseContainerSupport", "-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2", "-jar", "/opt/app/app.jar"]
