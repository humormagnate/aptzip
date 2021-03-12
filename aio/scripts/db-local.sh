#!/usr/bin/env bash

sudo docker run
--name aptzip-mysql \
--publish 13306:3306 \
--detach \
--restart=always \
--env MYSQL_ROOT_PASSWORD=testmaria \
--env TZ=Asia/Seoul \
--volume $(PWD)/aio/mysql/docker-entrypoint-initdb.d/:/docker-entrypoint-initdb.d/ \
--volume $(PWD)/aio/mysql/my.cnf:/etc/mysql/conf.d/aptzip.cnf,ro \
mysql:8.0.23
