# This Docker file uses the builder pattern with two stages Development and production
# For more information see: https://medium.com/@kaperys/create-lean-docker-images-using-the-builder-pattern-37fe2b5d97d4

# Define the development environment
FROM openjdk:11-jdk-slim as development-environment

# Copy and build the project (TODO: Use a volume to do this)
COPY . ./workspace
RUN cd /workspace \
    && ./gradlew flywayMigrate -i \
    && ./gradlew clean build -x test

# Define the production environment
FROM openjdk:11-jre-slim

# Copy the application
COPY --from=development-environment /workspace/build/libs/fhnwwodss.jar /opt/app/app.jar

# Start the application
CMD ["java", "-XX:+UseContainerSupport", "-Xmx300m", "-jar", "/opt/app/app.jar"]
