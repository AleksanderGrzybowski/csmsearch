FROM openjdk:8-jdk

COPY . /app
WORKDIR /app

RUN ./gradlew clean shadowJar

FROM openjdk:8-jre-alpine

COPY --from=0 /app/build/libs/csmsearch-1.0-SNAPSHOT-all.jar /app.jar
COPY ./docker-entrypoint.sh /
COPY ./db.txt /
RUN chmod +x /docker-entrypoint.sh

WORKDIR /

CMD '/docker-entrypoint.sh'
