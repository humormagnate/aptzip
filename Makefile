VERSION := 0.1.0

clean:
	@./mvnw clean
.PHONY: clean

db-local:
	sudo docker run \
		--name aptzip-mysql \
		--publish 13306:3306 \
		--detach \
		--restart=always \
		--env MYSQL_ROOT_PASSWORD=testmaria \
		--env TZ=Asia/Seoul \
		--volume $(PWD)/aio/mysql/docker-entrypoint-initdb.d/:/docker-entrypoint-initdb.d/ \
		--volume $(PWD)/aio/mysql/my.cnf:/etc/mysql/conf.d/aptzip.cnf,ro \
		mysql:8.0.23
.PHONY: db-local

test-all:
	@./mvnw test
.PHONY: test-all

# ./mvnw test -Dtest=UserAccountServiceTests
# ./mvnw test -Dtest=UserAccountServiceTests#testFindById
# make test-unit TEST_UNIT=UserAccountServiceTests#testFindById
test-unit:
	./mvnw test -Dtest=${TEST_UNIT}
.PHONY: test-unit

build-with-test: clean
	@./mvnw package
.PHONY: build-with-test

build: clean
	@./mvnw package -DskipTests
.PHONY: build

docker: build
	./aio/scripts/docker.sh
.PHONY: docker

#./mvnw spring-boot:run -D spring-boot.run.profiles=dev -D spring.config.location=file:application.yml
#./mvnw spring-boot:run -D spring.config.location=classpath:/application-dev.yaml
run:
	@./mvnw spring-boot:run
.PHONY: run

jar:
	@java -jar "-Dspring.profiles.active=dev" target/aptzip-${VERSION}.jar
.PHONY: jar

deps:
	./mvnw dependency:analyze
.PHONY: deps
