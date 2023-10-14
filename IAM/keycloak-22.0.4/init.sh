docker keycloak-container stop
docker rm keycloak-container
docker rmi keycloak-image

docker build -t keycloak-image .

ip=$(hostname -I | awk '{print $1}')

docker run --name keycloak-container -d -p 8180:8080 -e --db=mysql -e --db-password="my-secret-pw" -e --db-url=jdbc:mysql://$ip:3306/Keycloak -e --db-username=root -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD="my-secret-pw" keycloak-image start-dev --spi-theme-welcome-theme=keycloak