# —— build：打包 Vue ——
FROM node:20 AS build
WORKDIR /app

# 强制重新构建：添加构建标识
RUN echo "Build timestamp: $(date)" > /build-info.txt

COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build  # 生成 dist/

# —— run：Nginx 托管 + 反代 ——
FROM nginx:alpine
RUN apk add --no-cache gettext
# 用模板，启动时把变量替换进来
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf.template
COPY --from=build /app/dist /usr/share/nginx/html
ENV PORT=80
# BACKEND_HOST 例：defiant-marcelline-baihanrui-b9820c9e.koyeb.app
CMD sh -c "envsubst '\$PORT \$BACKEND_HOST' </etc/nginx/conf.d/default.conf.template >/etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"
