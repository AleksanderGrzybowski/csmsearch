FROM openjdk:8-jdk

COPY . /app
WORKDIR /app

RUN ./gradlew clean shadowJar

RUN ls -l /app/build/libs/
FROM openjdk:8-jre-alpine

RUN ls -l /app/build/libs/
COPY --from=0 /app/build/libs/doublewords-all.jar /
COPY ./docker-entrypoint.sh /
COPY ./db.txt /
RUN chmod +x /docker-entrypoint.sh

WORKDIR /

CMD '/docker-entrypoint.sh'
