FROM node:20-alpine AS frontend-build
WORKDIR /frontend
# Устанавливаем зависимости с кешем
COPY whisper-frontend2/package*.json ./
RUN npm ci --quiet
COPY whisper-frontend2/ ./
RUN npm run build

FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=frontend-build /frontend/dist/ /usr/share/nginx/html/