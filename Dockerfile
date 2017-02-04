FROM java
MAINTAINER tanmay ambre
COPY ./target/interacservice-0.0.1-SNAPSHOT.jar /usr/local/interacservice/
EXPOSE 6000
VOLUME /logs
WORKDIR /usr/local/interacservice
ENTRYPOINT java -jar interacservice-0.0.1-SNAPSHOT.jar > /logs/interacservice.system.out.log


