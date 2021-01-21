FROM openjdk:11.0.9
ARG JAR_FILE
COPY $JAR_FILE aptzip.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=server", "-Dspring.config.location=file:/application.yaml", "/aptzip.jar" ]
