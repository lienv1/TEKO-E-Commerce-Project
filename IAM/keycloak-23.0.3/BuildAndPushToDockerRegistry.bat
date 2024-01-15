call docker rmi keycloak-image
call docker build -t keycloak-image .
call docker tag keycloak-image ghcr.io/lienv1/webshopproject2/keycloak-image:version1.0.0
call docker push ghcr.io/lienv1/webshopproject2/keycloak-image:version1.0.0