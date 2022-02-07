# CalculatorServiceApplication
Regular Calculator project for basic arithmetic operations.

## Local Setup
* Clone the project and build locally. On application startup, it runs on default `8080` port.
* Swagger Link: `http://localhost:8080/swagger-ui/index.html`
* Swagger Documentation: `http://localhost:8080/v3/api-docs`
* Tech Stack for local setup: `Maven, JDK 8, IntelliJ/ Eclipse/ Any IDE, GIT`
* Build project and run application main class `CalculatorServiceApplication`

## Endpoints
* `add:` To perform addition operation. 
  * Example: `http://localhost:8080/calculate/add?augend=1&addend=2`
* `subtract:` To perform subtraction operation. 
  * Example: `http://localhost:8080/calculate/subtract?minuent=3&subtrahend=4`
* `multiply:` To perform multiplication operation. 
  * Example: `http://localhost:8080/calculate/multiply?multiplier=5&multiplicand=6`
* `divide:` To perform division operation. 
  * Example: `http://localhost:8080/calculate/divide?dividend=7&divisor=8`
* `history:` To check history of performed operation. 
  * Example: `http://localhost:8080/calculate/history`

## Data Store Configuration
* By Default, file data store is used, in order to persist the operations hisotory.
* It is configurable, by changing value of `calculator.data.store` in `application.properties` file. Valid values are `[file, db]`.
* By switching to db store, we can enable default in memory H2 db store for persistence.
* Open to configure any of the data source in order to switch to any other relational database. `(Future Implementation Scope)`
* Datasource configuration in application properties can be changed to switch to other database.
* NoSQL db can also be configured by adding further configurations and matching repository layer. `(Future Implementation Scope)`

## Cache Configuration
* At present, Default Cache is used with help of HashMap in memory, which gets loaded on application startup, and gets updated on any new operation.
* Avoid duplicate calls: It helps from db or file hit in order to perform existing operation retry by user, and returns value from map itself.
* Open to implement other Caching strategy. `(Future Implementation Scope)`

## Design
* Spring Boot based application, where the endpoints are defined in controller layer.
* Calculator Controller connects with Cache Service to pull the result of the operation.
* Cache layer validates if operation is already occurred in past and returns cached result.
* If it's a new operation, cache service performs action and connects to actual Calculator Service to persist the result. And it updates cache with the result and returns the same result.
* Depending on the enabled data store, Calculator Service connects to file store or db store.
* Flow:
  * `CalculatorController -> CalculatorCacheService [CalculatorDefaultCacheService] -> CalculatorService [CalculatorFileStoreService, CalculatorDBStoreService (CalculatorRepository)]`