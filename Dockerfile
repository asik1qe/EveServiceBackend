FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
# ВАЖНО: берём НЕ plain и НЕ original
COPY --from=build /app/target/*.jar /tmp/
RUN ls -la /tmp && \
    JAR=$(ls /tmp/*.jar | grep -vE '(plain|original)' | head -n 1) && \
    echo "Using jar: $JAR" && \
    mv "$JAR" /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]