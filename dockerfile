FROM openjdk:18-slim as build
MAINTAINER ivanMilev
COPY /target/product-0.0.1-SNAPSHOT.jar product-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/product-0.0.1-SNAPSHOT.jar"]