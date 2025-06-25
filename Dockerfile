FROM node:17.3.0

COPY ./csmsearch /frontend
WORKDIR /frontend
RUN npm install && npm run build

FROM nginx:1.29.0

COPY --from=0 /frontend/build/ /usr/share/nginx/html

