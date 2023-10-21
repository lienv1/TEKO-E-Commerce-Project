#!/bin/bash

# Get the local IPv4 address
localIP=$(hostname -I | awk '{print $1}')

if [ -z "$localIP" ]; then
    echo "No IPv4 address found"
    exit 1
fi

# Execute command in the Docker container
command="/opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/exportedRealm --db-url=jdbc:mysql://$localIP/Keycloak --db-username root --db-password my-secret-pw --users same_file"

docker exec -it keycloak-container /bin/sh -c "$command"

# Check if the docker exec command was successful
if [ $? -ne 0 ]; then
    echo "An error occurred in the docker exec command"
    exit 1
fi

# Copy data from container to the current location
currentDir=$(pwd)
docker cp keycloak-container:/opt/keycloak/data/exportedRealm $currentDir/backup/

# Check if the docker cp command was successful
if [ $? -ne 0 ]; then
    echo "An error occurred in the docker cp command"
    exit 1
fi
