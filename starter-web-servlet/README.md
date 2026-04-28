## Running the app locally

From the project root:

```cmd
./gradlew clean starter-web-servlet:run
```

And visit http://localhost:8080/application-types

You can view the Swagger UI definition at: 

http://localhost:8080/swagger/views/swagger-ui

Or with RapiDoc:

http://localhost:8080/swagger/views/rapidoc

Or with ReDoc:

http://localhost:8080/swagger/views/redoc

Or with OpenAPI Explorer:

http://localhost:8080/swagger/views/openapi-explorer

## Deployment

You can build the WAR file with:

```cmd
./gradlew clean starter-web-servlet:assemble
```

The WAR file will be located in `starter-web-servlet/build/libs` and can be deployed to any modern Servlet container (example Jetty 9 or Tomcat 9).
