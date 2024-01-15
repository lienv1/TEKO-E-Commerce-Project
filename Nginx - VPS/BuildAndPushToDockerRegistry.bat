call docker rmi nginx-image
call docker build -t nginx-image .
call docker tag nginx-image ghcr.io/lienv1/webshopproject2/nginx-image:version1.0.0
call docker push ghcr.io/lienv1/webshopproject2/nginx-image:version1.0.0