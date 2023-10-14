docker mysql-container stop
docker rm mysql-container
docker rmi mysql:latest

docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD=my-secret-pw -v "$(pwd)/mysql-data:/var/lib/mysql" -e MYSQL_DATABASE=Apiserver -p 3306:3306 mysql:latest

check_container() {
  echo "Create Keycloak database"
  docker exec -it mysql-container /bin/bash -c "mysql -u root -pmy-secret-pw -e 'CREATE DATABASE IF NOT EXISTS Keycloak;'"

  if [ $? -ne 0 ]; then
    echo "Try again... Please wait..."
    sleep 5
    check_container
  fi
}

check_container