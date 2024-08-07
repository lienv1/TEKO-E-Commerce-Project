# Clean and build with Maven
mvn clean package
# Build the Docker image
docker stop emailservice-container
docker rm emailservice-container
docker rmi emailservice-image
docker build -t emailservice-image .
# Run the Docker container

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name emailservice-container -d -e HOSTNAME="$localIP" -e SERVICE_HTTPS_ENABLED=false -e SPRING_MAIL_USERNAME=bobby.lien@achau.ch -p 8081:8081 emailservice-image