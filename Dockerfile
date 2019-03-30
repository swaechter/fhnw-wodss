# Note: Use the two stage docker buildern pattern: First use a fat development image to build the project and second use a lighter runtime image
# Starting point: https://blog.alexellis.io/mutli-stage-docker-builds/

# Use the fat development image
FROM openjdk:11-jdk-slim as development-environment

# Copy the whole project (TODO: Use a volume to do this)
COPY . /workspace

# Build the project
RUN cd /workspace && ./gradlew build -x test

# Now define the production image
FROM openjdk:11-jre-slim

# Copy all development environment artifacts
COPY --from=development-environment /workspace/build/libs/fhnw-wodss-0.0.1-SNAPSHOT.jar /app/fhnw-wodss.jar

# Start the application
WORKDIR /app
EXPOSE 8000
CMD ["java", "-jar", "fhnw-wodss.jar"]
