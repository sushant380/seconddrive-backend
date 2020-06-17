
# Second drive

### Java, Maven, Spring Boot, Spring Security, Rest API, Swagger 2, Mongo DB, Docker

This is an test project to generate Repayment plan for loan.

## Requirements

1. Java 8

2. Maven 

3. Spring Boot

4. Swagger 

5. Lombok 

6. Security

7. Docker

8. Mongo DB

## Steps to Setup

**1. Install the application to fetch dependencies.**
```bash
mvn install
```
In case you are facing compilation errors for domain beans, Please check the **lombok dependency** supported in your IDE. This should not be a problem from command line.

**2. Run the app using maven**

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.
You can use inbuilt swagger ui to test the app or you go for the postman/curl clients.

Swagger: <http://localhost:8080/swagger-ui.html>.
Swagger-docs:<http://localhost:8080/v2/api-docs>.

## Docker Compose
The docker compose builds mongodb and seconddrive backend service. It also connects to seconddrive-frontend<https://github.com/sushant380/seconddrive-frontend>. Seconddrive frontend should build an image separately, Dockerfile is include in the seconddrive frontend.

+ Applications are running on following ports under docker containers.
  Frontend: <http://localhost:9090> 
  backend: <http://localhost:8080>
  MongoDB: localhost:27017 
```bash
docker-compose up
```  
## Explore Rest APIs

The app defines following APIs.
    
    GET localhost:8080/cars (fetch cars)
    GET localhost:8080/cars/{1} (fetch car by id)
    GET localhost:8080/cars/make/{make} (fetch cars by make)
    POST localhost:8080/cars/search (fetch cars criteria, order and sort)
    GET localhost:8080/cars/search?q= (fetch cars by query string)
    


You can test them using postman or any other rest client.

    You must to add Basic Auth in your rest client. 

+ open `RestSecurityConfiguration`and you can find 2 roles and credentials
    
    Authorized user: admin/admin
    Unauthorized user: user/user

## Key points to note

+ API implementation and validation of parameters.
+ Supports Security /cars** requests
+ Use lombok library 
+ Mongo DB integration
+ Docker compose for frontend, backend and mongodb

## Possible improvements / out of scope

+ Rest documentation.
+ Logs.



