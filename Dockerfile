FROM maven:3.9.6-eclipse-temurin-21 as build

COPY . /intershop/

RUN mvn -f /intershop/pom.xml clean package -DskipTests
