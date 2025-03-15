# OpenFDA Drug Application API

## Overview
This project is a RESTful API built with Java and Spring Boot that integrates with the OpenFDA API to fetch and store drug applications. The API supports searching for FDA drug applications, storing selected applications in a database, and retrieving stored applications with pagination.

## Features
- Search for drug applications from OpenFDA API by manufacturer name and optional brand name
- Store selected drug applications in a local database
- Retrieve stored drug applications with pagination
- API documentation with Swagger

## Technologies Used
- Java 23
- Spring Boot 3
- Spring Data JPA
- H2 (in-memory database for local development)
- OpenAPI/Swagger for API documentation
- Maven for build and dependency management

## Prerequisites
- Java 23+
- Maven 3+

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repository/openfda-api.git
   cd openfda-api
   ```

2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

## Running the Application
1. Start the application:
   ```sh
   mvn spring-boot:run
   ```
2. The application will be available at:
   ```
   http://localhost:8080
   ```

## API Documentation
Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints
### 1. Search Drug Applications
**GET** `/drugs/search`
- **Query Parameters:**
    - `manufacturer` (required) - Manufacturer name
    - `brand` (optional) - Brand name
    - `limit` (optional, default: 10) - Number of records per page
    - `skip` (optional, default: 0) - Number of records to skip

**Example:**
```
GET http://localhost:8080/drugs/search?manufacturer=Renew Pharmaceuticals
```

### 2. Store Drug Application
**POST** `/drugs/save`
- **Request Body:**
```json
{
  "applicationNumber": "ANDA040811",
  "manufacturerName": "Renew Pharmaceuticals",
  "substanceName": "INDOCYANINE GREEN",
  "productNumbers": ["73624-424", "70100-424"]
}
```

### 3. Retrieve Stored Applications (Paginated)
**GET** `/drugs/stored?page=0&size=10`

## Running Tests
To run tests, execute:
```sh
mvn test
```

## Database Configuration
The application uses an H2 in-memory database by default. To use a persistent database, update `application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/openfda
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## Deployment
To package the application as a JAR file:
```sh
mvn package
```
Run the packaged JAR:
```sh
java -jar target/openfda-api-0.0.1-SNAPSHOT.jar
```

## License
This project is open-source and available under the MIT License.

