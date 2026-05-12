# Multi-stage build: Build JAR in Maven container, then run in JRE container

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /build/target/security-log-analyzer-2.0.0.jar .

# Copy sample data files
COPY logins.txt .
COPY HOW_TO_ADD_DATA.txt .
COPY README.md .

# Set permissions
RUN chmod +x /app/security-log-analyzer-2.0.0.jar

# Expose port (for future web service features)
EXPOSE 8080

# Default command - Run the GUI application
# Note: For GUI on Windows/Mac, use: docker run -e DISPLAY=host.docker.internal:0 ...
# For Linux X11: docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY ...
ENTRYPOINT ["java", "-jar", "security-log-analyzer-2.0.0.jar"]

# For headless/CLI mode override with:
# docker run security-log-analyzer mvn exec:java -Dexec.mainClass="com.soc.analyzer.LogAnalyzer"
