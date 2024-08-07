call docker mysql-container stop
call docker rm mysql-container

@echo off
call docker run --name mysql-container -d -e MYSQL_ROOT_PASSWORD=my-secret-pw -e MYSQL_DATABASE=Apiserver -v "%cd%/mysql-data:/var/lib/mysql" -p 3306:3306 mysql:8.1.0

:check_container
echo "Setup..."
timeout /t 5
echo "Create keycloak database"
call docker exec -it mysql-container /bin/sh -c "mysql -u root -pmy-secret-pw -e 'CREATE DATABASE IF NOT EXISTS Keycloak;'"
if errorlevel 1 (
    echo "Try again... Please wait..."
    timeout /t 5 /nobreak >nul
    goto check_container
)
