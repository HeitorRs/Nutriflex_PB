FROM amazoncorretto:21
LABEL author="heitorramos"
WORKDIR ./app
COPY Eureka_Server-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]