call docker exec -it keycloak-container /bin/sh -c "/opt/keycloak/bin/kc.sh export --dir /opt/keycloak/bin/exportedRealm --db-url=jdbc:mysql://192.168.1.129/Keycloak --db-username root --db-password my-secret-pw --users same_file"
call docker cp keycloak-container:/opt/keycloak/bin/exportedRealm %cd%/backup/