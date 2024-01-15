call mvn clean package
call docker rmi emailservice-image
call docker build -t emailservice-image .
call docker tag emailservice-image ghcr.io/lienv1/webshopproject2/emailservice-image:version1.0.0
call docker push ghcr.io/lienv1/webshopproject2/emailservice-image:version1.0.0