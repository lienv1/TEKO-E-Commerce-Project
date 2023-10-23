#!/bin/bash

# Stop running container
docker stop express-container

# Remove stopped container
docker rm express-container

# Remove image used by the container
docker rmi express-image

# Build a new image (the command should be 'build', not 'built')
docker build -t express-image .

# Get the local IPv4 address
ip=$(hostname -I | awk '{print $1}')

# Run a new container from the image
docker run --name express-container -d -p 3000:3000 -v "$(pwd)/Files:/usr/src/app/Files" -e KEYCLOAK_URL="http://${ip}:8180" express-image
