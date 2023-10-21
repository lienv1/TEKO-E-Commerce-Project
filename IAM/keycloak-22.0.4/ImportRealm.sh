#!/bin/bash

# Get the local IPv4 address
localIP=$(hostname -I | awk '{print $1}')

if [ -z "$localIP" ]; then
    echo "No IPv4 address found"
    exit 1
fi

# Construct the command with proper quoting
command="/opt/keycloak/bin/kc.sh import --dir /opt/keycloak/data/exportedRealm --override false --db-url=jdbc:mysql://$localIP:3306/Keycloak --db-username root --db-password my-secret-pw"

# Run the command in the container
docker exec -it keycloak-container /bin/bash -c "$command"

# Check if the docker exec command was successful
if [ $? -ne 0 ]; then
    echo "An error occurred in the docker exec command"
    exit 1
fi
