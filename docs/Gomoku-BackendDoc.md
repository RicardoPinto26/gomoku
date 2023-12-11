## Introduction

This project was a RESTful API to be used by a frontend client application, with the following functionalities:
* User authentication
* Lobby creation, listing and joining
* Game creation and forfeit

## Modeling the database

### Conceptual model ###

### Physical Model ###

### Open-API Specification ###


## Application Architecture ##

The backend is organized as follows:
* [`/domain`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/domain): contains the classes and functions representing domain concepts;
* [`/http`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/http): contains functions and types responsible for exposing and implementing the HTTP API, implemented with Spring MVC;
* [`/repository`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/repository): contains functions and types to interact with the RDBMS and its database , implemented using JDBI;
* [`/services`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/services): contains the components that deal with the business logic of the application;
* [`/utils`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/utils): contains the utility classes used in the application;
* [`Environment`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/Environment.kt): contains the function that collects the database url from system variables;
* [`GomokuRoyaleApplication`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/GomokuRoyaleApplication.kt): The entry point for the gomoku application

## HTTP Group ##

The HTTP group contains functions and types responsible for exposing and implementing the HTTP API.
The **data type** used in these requests and response where DTOs(**Data Transfer Objects**).

This group was implemented using **Spring MVC**

This group is composed of:
* [`/controllers`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/http/controllers): contains the controllers that manage the HTTP requests;
* [`/media`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/http/media): contains the classes that represent the media types used in the application;
* [`/pipeline`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/http/pipeline): contains the pipeline that processes the HTTP requests;
* [`/utils`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/http/utils): contains utility objects, such as Actions, Links, Uris and extension functions to convert errors to responses

## Service Group ##
The service group is responsible for receiving request from the HTTP Group and requesting the appropriate data from
the repository group to supply a response. The **data types** used in these requests and responses where 
DTOs(**Data Transfer Objects**).

Each service is responsible for managing a specific group of requests, and each service is annotated with a @Component 
annotation for further injection into it's corresponding controller.

Each service has an interface that defines the methods that it implements, and the implementation of the interface. 

The service group is organized as follows:
* [`/game`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/services/game): contains the business logic for the **Game** entity and the corresponding DTOs;
* [`/lobby`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/services/lobby): contains the business logic for the **Lobby** entity and the corresponding DTOs;
* [`/users`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/services/users): contains the business logic for the **User** entity and the corresponding DTOs;


## Repository Group ##

The repository group defines types and functions to interact with the persisted data namely the data managed by the RDBMS
and is implemented using **JDBI**.

The **data types** used in this group are the domain classes, which are mapped from a given result set of the database.

This group is composed by:
* [`/game`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/repository/game): contains functions to manipulate data concerning the **Game** entity in the database;
* [`/lobby`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/repository/jdbi): contains the transaction Manager and the mappers needed to map the result set to domain classes;
* [`/user`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/repository/lobby): contains functions to manipulate data concerning the **Lobby** entity in the database;
* [`/jdbi`](../code/jvm/src/main/kotlin/pt/isel/leic/daw/gomokuRoyale/repository/user): contains functions to manipulate data concerning the **User** entity in the database;


## Authentication ##

## Error Handling ##

## Running the Application ##