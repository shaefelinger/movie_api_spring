# Movie-API

This is an implementation of the MovieAPI from The SeniorDev Fullstack-Thinking ActionItem in Java/Springboot

### Setup
Run the Postgres-Docker-Container (db runs on port 5431 )

```bash
docker compose up
```

Start the App (Maven and Java 17 has to be installed on the machine)
```bash
 mvn spring-boot:run
```

## Features

- Spring-JPA as ORM
- Swagger
  - http://localhost:8080/swagger-ui/index.html#/
  - JSON: http://localhost:8080/v3/api-docs
- Compression
- CORS
- Migrations with Liquibase
- Basic Auth (admin/supersecret)
- logging
- Actuator Health-Check
  - http://localhost:8080/health
  - http://localhost:8080/prometheus

