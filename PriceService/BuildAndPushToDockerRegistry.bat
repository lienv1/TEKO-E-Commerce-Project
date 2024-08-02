call mvn clean package
call docker rmi priceservice-image
call docker build -t priceservice-image .
call docker tag priceservice-image ghcr.io/lienv1/webshopproject2/priceservice-image:version1.0.1
call docker push ghcr.io/lienv1/webshopproject2/priceservice-image:version1.0.1