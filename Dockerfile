FROM maven:3.9.6-eclipse-temurin-21 as build

COPY . /intershop/

RUN mvn -f /intershop/pom.xml clean package -DskipTests

FROM openjdk:21

WORKDIR /intershop

COPY --from=build /intershop/backend/target/*.jar ./intershop.jar

CMD ["java", "-jar", "/intershop/intershop.jar"]
