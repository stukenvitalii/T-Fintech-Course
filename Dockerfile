# Stage 1: Build the application
FROM gradle:8.4-jdk17 AS build

# Set the working directory
WORKDIR /application

# Copy Gradle files and wrapper
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/

RUN chmod +x ./gradlew

# Run Gradle to build the application and generate the JAR file
RUN ./gradlew build

# Stage 2: Extract dependencies using layertools
FROM amazoncorretto:19-alpine AS layers

WORKDIR /application

# Copy the JAR file from the build stage
COPY --from=build /application/build/libs/*.jar app.jar

# Extract dependencies using layertools
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Run the application
FROM amazoncorretto:19-alpine

VOLUME /tmp

# Create a non-root user
RUN adduser -S spring-user
USER spring-user

# Copy the extracted dependencies from the 'layers' stage
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

# Set the entry point to launch the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
