# Stage 2: Use the official NGINX base image
FROM nginx

# Copy your custom NGINX configuration to the container
COPY conf/nginx.conf /etc/nginx/nginx.conf
# Copy your SSL certificate and key files to the container
COPY ssl/fullchain.pem /etc/nginx/ssl/
COPY ssl/privkey.pem /etc/nginx/ssl/


# Expose port 80 (the default port for HTTP)
EXPOSE 80
# Expose port 443 for https
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]