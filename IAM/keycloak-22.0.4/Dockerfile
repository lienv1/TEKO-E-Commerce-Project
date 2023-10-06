FROM quay.io/keycloak/keycloak:22.0.4 as builder

WORKDIR /opt/keycloak
RUN /opt/keycloak/bin/kc.sh build

COPY themes /opt/keycloak/themes

# ENV KC_DB=mysql
# ENV KC_DB_URL=jdbc:mysql://localhost:3306/Keycloak
# ENV KC_DB_USERNAME=root
# ENV KC_DB_PASSWORD=my-secret-pw
# ENV KC_HOSTNAME=localhost
ENV KC_PROXY=edge
ENV KC_HTTP_ENABLED=true
ENV KC_HOSTNAME_STRICT=false
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]