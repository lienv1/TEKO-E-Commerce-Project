docker stop nginx-container
docker rm nginx-container --force
docker rmi nginx-image --force
docker build -t nginx-image .

# Get the local IPv4 address
$localIP = (Get-NetIPAddress | Where-Object { $_.AddressFamily -eq 'IPv4' -and $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

docker run --name nginx-container -e NGINX_ENVSUBST_OUTPUT_DIR=/etc/nginx -e PROXY_PASS_URL="$localIP" -e PROXY_GUI_URL="$localIP":4200 -e PROXY_API_URL="$localIP":8080 -e PROXY_FILE_URL="$localIP":3000 -e PROXY_AUTH_URL="$localIP":8180 -p 80:80 -p 443:443 -d nginx-image