FROM node:17.3.0

COPY ./csmsearch /frontend
WORKDIR /frontend
RUN npm install && npm run build

FROM httpd:latest

COPY --from=0 /frontend/build/ /usr/local/apache2/htdocs/

