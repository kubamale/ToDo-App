# ToDo App
Simple application that allows tracking of tasks that should be completed and those that are allready completed.
## Used Technologies
### Backend
* Java
* Spring Boot
* Spring Data Jpa
* Test Containers
* Lombok
* Mapstruct
* PostgresSql
### Frontend
* Angular
## How To Run
### Backend
In the .env file in projects root directory, fill in your database user and password.
```text
DATABASE_PASSWORD=<databse_password>
DATABASE_USER=<databse_user>
```
#### Requirements
- Docker
```shell
docker compose up
```
### Frontend
#### Requirements
- Angular 17
```shell
cd frontend/
ng serve
```

## Functionalities
- Crud operations on tasks
- Amount of received requests (http://localhost:8080/actuator/metrics/http.server.requests)
- Swagger Documentation (http://localhost:8080/swagger-ui/index.html)