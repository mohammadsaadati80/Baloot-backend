# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS MAVEN
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

## Stage 2: Package and run
FROM openjdk:17
COPY --from=MAVEN /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar" ,"app.jar"]
