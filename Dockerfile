# # Multi-stage

# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS MAVEN
MAINTAINER BALOOT_DEVELOPER
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package


## Stage 2: Package and run
FROM openjdk:17
WORKDIR /app
COPY --from=MAVEN /build/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar" , "app.jar"]




# FROM maven:3.8.5-openjdk-17-slim AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package

# #
# #Package stage
# FROM adoptopenjdk/openjdk17
# COPY --from=build /home/app/target/baloot-0.0.1-SNAPSHOT.jar /usr/local/lib/baloot.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/usr/local/lib/baloot.jar"]




# FROM maven:3.8.5-openjdk-17 as build
# WORKDIR /tmp
# COPY pom.xml /tmp
# COPY src /tmp/src
# RUN mvn -Dhttps.protocols=TLSv1.2 package

# FROM openjdk:17-jre-slim
# WORKDIR /app
# COPY --from=build /tmp/target/*.jar /app/app.jar

# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/app/app.jar"]







# # Stage 1: Build
# FROM maven:3.8.5-openjdk-17 AS MAVEN
# MAINTAINER BALOOT_DEVELOPER
# COPY pom.xml /build/
# COPY src /build/src/
# WORKDIR /build/
# RUN mvn package

# FROM adoptopenjdk/openjdk17
# COPY --from=build /home/app/target/baloot-0.0.1-SNAPSHOT.jar /usr/local/lib/baloot.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/usr/local/lib/baloot.jar"]