# Start with a base image containing Java runtime and Maven
FROM maven:3.8.5-openjdk-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and external JARs
COPY pom.xml ./
COPY libs ./libs

# Copy the source code into the container
COPY src ./src

# Run Maven package to build the application, without going offline
RUN mvn package -DskipTests

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the application with Spring Boot
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "target/untitled-1.0-SNAPSHOT.jar"]
