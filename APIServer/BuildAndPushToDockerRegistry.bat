call mvn clean package
call docker rmi api-image
call docker build -t api-image .
call docker tag api-image ghcr.io/lienv1/webshopproject2/api-image:version1.0.0
call docker push ghcr.io/lienv1/webshopproject2/api-image:version1.0.0