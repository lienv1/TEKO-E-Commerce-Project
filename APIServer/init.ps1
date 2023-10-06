# Clean and build with Maven
mvn clean package test
# Build the Docker image
docker build -t api-image .
# Run the Docker container

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name api-server-container -d -e HOSTNAME="$localIP" -p 8080:8080 api-image