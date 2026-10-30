# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy the whole project (the server module depends on :shared)
COPY . .

# Build the fat jar for the server module only (needs the io.ktor.plugin Gradle plugin)
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x ./gradlew && \
    ./gradlew :server:buildFatJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/server/build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]