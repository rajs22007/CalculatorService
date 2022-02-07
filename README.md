# CalculatorService
Regular Calculator project for arithmetic operations

## Local Setup
Clone the project and build locally. Default on application startup it runs on 8080 port.
#### Swagger Link: `http://localhost:8080/swagger-ui/index.html`
#### Swagger Documentation: `http://localhost:8080/v3/api-docs`

## Endpoints
#### add: To perform addition operation. Example: `http://localhost:8080/calculate/add?augend=1&addend=2`
#### subtract: To perform subtraction operation. Example: `http://localhost:8080/calculate/subtract?minuent=3&subtrahend=4`
#### multipy: To perform multiplication operation. Example: `http://localhost:8080/calculate/multiply?multiplier=5&multiplicand=6`
#### divide: To perform division operation. Example: `http://localhost:8080/calculate/divide?dividend=7&divisor=8`
#### history: To check hisotry of performed operation. Example: `http://localhost:8080/calculate/history`

## Data Store Configuration
#### By Default, file data store is used, in order to persist the operations hisotory.
#### It is configurable, by changing value of `calculator.data.store` in `application.properties` file. Valid values are `[file, db]`.
#### By switching to db store, we can enable default in memory H2 db store for persistence.
#### Open to configure any of the data source in order to switch to any other relational database. `(Future Implementation Scope)`
#### Datasource configuration in application properties can be changed to switch to other database.
#### NoSQL db can also be configured by adding further configurations and matching repository layer. `(Future Implementation Scope)`

## Cache Configuration
#### At present, Default Cache is used with help of HashMap in memory, which gets loaded on application startup, and gets updated on any new operation.
#### Avoid duplicate calls: It helps from db or file hit in order to perform existing operation retry by user, and returns value from map itself.
#### Open to implement other Caching strategy. `(Future Implementation Scope)`
