FROM maven:3.8.4-openjdk-11

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN mvn package

COPY target/hml-0.0.1-SNAPSHOT.jar hml.jar
CMD ["java", "-jar", "/target/hml-0.0.1-SNAPSHOT.jar"]
