FROM java
MAINTAINER tanmay ambre
COPY ./target/interacservice-0.0.1-SNAPSHOT.jar /usr/local/interacservice/
EXPOSE 11020
VOLUME /logs
WORKDIR /usr/local/interacservice
ENTRYPOINT java -Xmx512m -jar interacservice-0.0.1-SNAPSHOT.jar > /logs/interacservice.system.out.log


