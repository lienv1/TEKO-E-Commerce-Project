# Stop running container
docker stop express-container

# Remove stopped container
docker rm express-container

# Remove image used by the container
docker rmi express-image

# Build a new image (the command should be 'build', not 'built')
docker build -t express-image .

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

# Run a new container from the image
docker run --name express-container -d -p 3000:3000 -v ${PWD}/Files:/usr/src/app/Files -e KEYCLOAK_URL=http://`"$localIP`":8180 express-image
