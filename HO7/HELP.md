# Hands on #7 Spring boot
### By
Víctor Ramón Carrillo Quintero

##How to start:
docker-compose build

docker-compose up

## That´s it
####Note 1: First time will fail because the database won't exist as soon as the container is, so spring boot won't be able to connect. Just re-execute first time. 
####If you modify something just do "gradlew build" before "how to start"
####To run without Docker (idky) just change the commented mysql line in application.properties