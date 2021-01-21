VERSION := 0.1.0

.PHONY: clean
clean:
	@mvn clean

.PHONY: build
build: clean
	@mvn package

.PHONY: build-skip-test
build-skip-test: clean
	@mvn package -DskipTests

.PHONY: docker
docker:
	./aio/scripts/docker.sh

.PHONY: run
run:
	#mvn spring-boot:run -D spring-boot.run.profiles=dev -D spring.config.location=file:application.yml
	mvn spring-boot:run -D spring-boot.run.profiles=dev

.PHONY: jar
jar:
	@java -jar "-Dspring.profiles.active=dev" target/aptzip-${VERSION}.jar

.PHONY: deps
deps:
	mvn dependency:analyze
