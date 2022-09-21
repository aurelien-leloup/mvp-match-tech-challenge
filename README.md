# The vending machine 🏧➡️🍬

This project was made part of the MVP Match onboarding process

## Prerequisites

- Node.js (v16+)
- Java (v18+) with JAVA_HOME env variable pointing to the installation


## Run the project

### Build the angular app

```bash
cd src/main/vending-machine-web
npm install
node ./node_modules/@angular/cli/bin/ng build --deploy-url /vending-machine/ --base-href /vending-machine
```

The output will be stored inside the resources of the Springboot application. 

### Run the java app

```bash
cd ../../..
./mvnw spring-boot:run
```
To test the web application navigate to : http://localhost:8080/vending-machine
The postman collection to test the backend is also included at the root of the project

## Database 
The project uses H2 in-memory database to store data. 

A web console will be available at : 
http://localhost:8080/vending-machine/h2console

Using the following credentials :

| Parameter    | Value              |
|--------------|--------------------|
| Driver Class | org.h2.Driver      |
| JDBC URL     | jdbc:h2:mem:testdb |
| User Name    | sa                 |
| Password     |                    |
