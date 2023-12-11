# Clean and build with Maven
mvn clean package test
# Build the Docker image
docker stop api-container
docker rm api-container
docker rmi api-image
docker build -t api-image .
# Run the Docker container

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name api-container -d -e HOSTNAME="$localIP" -p 8080:8080 api-imagedocker run --name api-container -d -e HOSTNAME="$localIP" -e SERVICE_HTTPS_ENABLED=false -p 8080:8080 api-image