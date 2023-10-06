# Clean and build with Maven
mvn clean package test

# Build the Docker image
docker build -t api-image .

# Run the Docker container
# Get the local IPv4 address using hostname
ip=$(hostname -I | awk '{print $1}')

# Run your Docker container with the obtained IP
docker run --name api-server-container -e CUSTOM_PROPERTY_HOST="$localIP" -p 8080:8080 api-image
