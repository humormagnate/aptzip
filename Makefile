VERSION := 0.1.0

.PHONY: clean
clean:
	@./mvnw clean

.PHONY: build
build: clean
	@./mvnw package -DskipTests

.PHONY: build-with-test
build-with-test: clean
	@./mvnw package

.PHONY: docker
docker: build
	./aio/scripts/docker.sh

.PHONY: run
run:
	@#./mvnw spring-boot:run -D spring-boot.run.profiles=dev -D spring.config.location=file:application.yml
	@./mvnw spring-boot:run -D spring-boot.run.profiles=dev

.PHONY: jar
jar:
	@java -jar "-Dspring.profiles.active=dev" target/aptzip-${VERSION}.jar

.PHONY: deps
deps:
	./mvnw dependency:analyze
