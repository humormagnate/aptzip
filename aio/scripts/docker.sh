#!/usr/bin/env bash

remove::docker_images() {
	image_hash=$(docker images | grep $BINARY | awk '{print $3}')
	if [ -z "$image_hash" ]; then
		echo "\$image_hash is NULL"
	else
		echo "\$image_hash is NOT NULL"
		docker rmi -f $image_hash
	fi
}

BINARY="aptzip"
IMAGE="aptzip"
VERSION="0.1.0"
REPO="cxsu"
REPO_PORT=""

remove::docker_images
# docker build --build-arg JAR_FILE=target/$BINARY-$VERSION.jar -t $IMAGE:$VERSION -f ./aio/Dockerfile .
docker build -t $IMAGE:$VERSION -f ./aio/Dockerfile .
docker tag $IMAGE:$VERSION $REPO$REPO_PORT/$IMAGE:$VERSION
docker tag $IMAGE:$VERSION $REPO$REPO_PORT/$IMAGE:latest

echo -e "\n>>> PRINT IMAGES <<<"
echo "$(docker images | grep $BINARY | awk '{ printf ("%s\n", $0) }')"

docker push $REPO$REPO_PORT/$IMAGE:$VERSION
