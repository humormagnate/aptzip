VERSION := 0.1.0

.PHONY: clean
clean:
	@mvn clean

.PHONY: build
build: clean
	@mvn package -DskipTests

.PHONY: build-with-test
build-with-test: clean
	@mvn package

.PHONY: docker
docker: build
	./aio/scripts/docker.sh

.PHONY: run
run:
	@#mvn spring-boot:run -D spring-boot.run.profiles=dev -D spring.config.location=file:application.yml
	@mvn spring-boot:run -D spring-boot.run.profiles=dev

.PHONY: jar
jar:
	@java -jar "-Dspring.profiles.active=dev" target/aptzip-${VERSION}.jar

.PHONY: deps
deps:
	mvn dependency:analyze
