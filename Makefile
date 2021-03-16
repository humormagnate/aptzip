VERSION := 0.1.0

clean:
	@./mvnw clean
.PHONY: clean

db-up:
	sudo docker-compose --file=docker-compose.yaml up --detach
	sudo docker logs -f aptzip-db
.PHONY: db-up

db-down:
	sudo docker-compose down
.PHONY: db-down

db-empty:
	sudo docker run \
		--name aptzip-mysql \
		--publish 13306:3306 \
		--detach \
		--restart=always \
		--env MYSQL_ROOT_PASSWORD=testmaria \
		--env MYSQL_DATABASE=aptzip \
		--env TZ=Asia/Seoul \
		--volume $(PWD)/aio/mysql/my.cnf:/etc/mysql/conf.d/aptzip.cnf,ro \
		mysql:8.0.23
	@sudo docker logs -f aptzip-db
.PHONY: db-empty

test-all:
	@./mvnw test
.PHONY: test-all

# ./mvnw test -Dtest=UserAccountServiceTests
# ./mvnw test -Dtest=UserAccountServiceTests#testFindById
# make test-unit TEST_UNIT=UserAccountServiceTests#testFindById
test-unit:
	./mvnw test -Dtest=${UNIT} #-X
.PHONY: test-unit

build-wo-test: clean
	@./mvnw package -DskipTests
.PHONY: build-wo-test

build: clean
	@# npm install
	@# npm run bundle
	./mvnw package
.PHONY: build

docker-build: build
	./aio/scripts/docker.sh
.PHONY: docker-build

docker-up:
	sudo docker-compose --file=docker-compose-web.yaml up --detach --build
.PHONY: docker-compose

docker-down:
	sudo docker-compose --file=docker-compose-web.yaml down
.PHONY: docker-compose

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
