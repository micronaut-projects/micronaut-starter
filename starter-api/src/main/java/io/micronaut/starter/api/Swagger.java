package io.micronaut.starter.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Starter",
                version = "1.0",
                description = "API for Creating Micronaut Applications",
                license = @License(name = "Apache 2.0")
        )
)
public class Swagger {
}
