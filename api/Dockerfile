FROM gradle:8.14-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test --project-dir /app

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]