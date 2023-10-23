# Stop running container
docker stop express-container

# Remove stopped container
docker rm express-container

# Remove image used by the container
docker rmi express-image

# Build a new image (the command should be 'build', not 'built')
docker build -t express-image .

# Run a new container from the image
docker run --name express-container -d -p 3000:3000 -v ${PWD}/Files:/usr/src/app/Files express-image
