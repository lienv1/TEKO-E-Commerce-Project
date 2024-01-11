# Clean and build with Maven
mvn clean package
# Build the Docker image
docker stop priceservice-container
docker rm priceservice-container
docker rmi priceservice-image
docker build -t priceservice-image .
# Run the Docker container

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name priceservice-container -d -e HOSTNAME="$localIP" -e SERVICE_HTTPS_ENABLED=false -p 8083:8080 priceservice-image