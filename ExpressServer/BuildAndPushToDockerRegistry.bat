call docker rmi express-image
call docker build -t express-image .
call docker tag express-image ghcr.io/lienv1/webshopproject2/express-image:version1.0.0
call docker push ghcr.io/lienv1/webshopproject2/express-image:version1.0.0