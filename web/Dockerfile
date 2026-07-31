ARG NODE_IMAGE=node:22-alpine
ARG NGINX_IMAGE=nginx:1.27-alpine

FROM ${NODE_IMAGE} AS build
WORKDIR /workspace
COPY package.json package-lock.json ./
COPY web/package.json ./web/package.json
RUN npm ci
COPY web ./web
RUN npm run frontend:build

FROM ${NGINX_IMAGE} AS runtime
ARG VERSION=0.6.0
ARG REVISION=unknown
ARG CREATED=unknown
ARG SOURCE=unknown

LABEL org.opencontainers.image.title="INT1433 Training Platform Web" \
      org.opencontainers.image.description="Vue static frontend and Nginx reverse proxy for INT1433 Training Platform" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.source="${SOURCE}" \
      org.opencontainers.image.revision="${REVISION}" \
      org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.created="${CREATED}"

COPY web/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/web/dist /usr/share/nginx/html

EXPOSE 80
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=5 CMD wget -qO- http://127.0.0.1/healthz >/dev/null || exit 1
