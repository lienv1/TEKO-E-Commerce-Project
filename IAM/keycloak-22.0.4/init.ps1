docker stop keycloak-container
docker rm keycloak-container
docker rmi keycloak-image

docker build -t keycloak-image .

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name keycloak-container -d -p 8180:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD="my-secret-pw" keycloak-image start-dev --db=mysql --db-password="my-secret-pw" --db-url=jdbc:mysql://"$localIP":3306/Keycloak --db-username=root --spi-theme-welcome-theme=keycloak