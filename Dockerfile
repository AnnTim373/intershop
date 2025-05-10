FROM maven:3.9.6-eclipse-temurin-21 as build

COPY . /intershop/

RUN mvn -f /intershop/pom.xml clean package

FROM openjdk:21

WORKDIR /intershop

COPY --from=build /intershop/target/*.jar ./intershop.jar

CMD ["java", "-jar", "/intershop/intershop.jar"]
