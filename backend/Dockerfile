FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S shopwise && adduser -S shopwise -G shopwise

COPY --from=build /workspace/build/libs/*.jar /app/shopwise-backend.jar

USER shopwise

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/shopwise-backend.jar"]
