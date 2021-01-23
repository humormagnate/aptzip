#!/usr/bin/env bash

VERSION=0.1.0
java -jar "-Dspring.profiles.active=dev" target/aptzip-${VERSION}.jar
