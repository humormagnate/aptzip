FROM openjdk:11.0.9
COPY target/aptzip-*.jar aptzip.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "-Dspring.config.location=file:/application.yaml", "/aptzip.jar" ]
