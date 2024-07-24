FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/OlxDeployed-0.0.1-SNAPSHOT.jar olxDeployed.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "olxDeployed.jar"]
