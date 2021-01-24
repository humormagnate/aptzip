FROM openjdk:11.0.9
ARG JAR_FILE
COPY $JAR_FILE aptzip.jar
COPY src/main/resources/application-dev.yaml application.yaml
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=dev", "-Dspring.config.location=file:/application.yaml", "/aptzip.jar" ]
