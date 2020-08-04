# Scrum_ally

[![keep growing logo](readme-images/logo_250x60.png)](https://keepgrowing.in)

Scrum_ally is a web application designed for project management.
This project is a multi-module app, built with Spring Boot and Angular. It can be built into a single jar file using Maven.

## Backend analysis

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=alert_status)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=security_rating)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=bugs)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=coverage)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=code_smells)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=little-pinecone_scrum-ally&metric=sqale_index)](https://sonarcloud.io/dashboard?id=little-pinecone_scrum-ally)

## Overview and technical features

![dashboard](readme-images/dashboard-page.png)

The project currently supports creating and managing tasks for multiple projects.

Some of the more interesting technologies used are:

* REST API
* JWT token-based authentication
* Role-based access to resources
* Flyway migrations
* Material design
* Pagination

## Getting Started

To clone this repository, execute the following in the command line:
```bash
$ git clone https://github.com/little-pinecone/scrum-ally.git
```

You can build the application with:
```bash
$ mvn clean install
```

To run fronted separately, on `localhost:4200`, got to the `/frontend/src/main/angular` directory and run:
```
$ npm start
```

## Database
Use the attached `docker-compose.yml` file and run:
```
$ docker-compose up -d
```
Use Flyway migrations, e.g. `backend/src/main/resources/db/migration/V1_2020_03_5.sql` to apply the proper db schema.

### Create a test user

POST Endpoint:

```bash
http://localhost:8080/api/users
```
Body:

```json
{
    "userCredentials": {
        "username": "user",
        "password": "test"
    }
}
```

## Running tests

Run all backend tests with the following command in the root directory:
```bash
$ mvn test
```
Run all frontend tests with the following command in the `frontend/src/main/angular` directory:
```bash
$ ng test
```

## Sonarqube

If you want to generate a SonarQube report locally, run:
```bash
$ mvn verify sonar:sonar -Pcode-coverage
```

## API documentation

To see the API docs generated by Swagger build and run the application, and visit the ```http://localhost:8080/swagger-ui.html``` link in your browser.

### Add the CSRF token to request from Postman

* Copy this code to the `Tests` tab for the mutable requests:

```
var xsrfCookie = postman.getResponseCookie("XSRF-TOKEN");
pm.globals.set('csrftoken', xsrfCookie.value);
```

![add csrf to postman screenshot](readme-images/postman-tests-with-csrf.png)

* Add the `X-XSRF-TOKEN` header that will use the `{{csrftoken}}` variable:

![add csrf to postman screenshot](readme-images/postman-add-header.png)

## Built With

* [Spring Boot 2.2.4](https://start.spring.io/)
* [Angular 10](https://angular.io/)
* [Maven](https://maven.apache.org/)
* [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin)
* [Daemonite's Material UI](https://daemonite.github.io/material/)

## Screenshots

![landing page](readme-images/landing-page.png)
![dashboard](readme-images/dashboard-page.png)
![project list](readme-images/project-list-page.png)
![project details](readme-images/project-details-page.png)
![side navigation](readme-images/side-nav-component.png)
![login page](readme-images/login-page.png)

## To do

* HATEOAS
* Managing project members, sharing projects
* Custom messages for successful and failed operations
* Breadcrumbs


## License

This project is licensed under the Unlicense - see the [license details](https://choosealicense.com/licenses/unlicense/).
