package io.micronaut.starter.feature.other

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class SwaggerUISpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test swagger-ui features"() {
        when:
        Features features = getFeatures(['swagger-ui'])

        then:
        features.contains("openapi")
    }

    void "test config without security feature"() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().features(['swagger-ui']).build())
        Properties properties = new Properties()
        properties.load(new StringReader(output['src/main/resources/application.properties']))

        then:
        properties.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        properties.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        properties.get('micronaut.router.static-resources.swagger-ui.paths') == "classpath:META-INF/swagger/views/swagger-ui"
        properties.get('micronaut.router.static-resources.swagger-ui.mapping') == "/swagger-ui/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=true"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=false"
        output["openapi.properties"].readLines()[3] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[4] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[5] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[6] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")
    }

    void "test config with security feature"() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().features(['swagger-ui', 'security']).build())
        Properties properties = new Properties()
        properties.load(new StringReader(output['src/main/resources/application.properties']))

        then:
        output.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        output.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        output.get('micronaut.router.static-resources.swagger-ui.paths') == "classpath:META-INF/swagger/views/swagger-ui"
        output.get('micronaut.router.static-resources.swagger-ui.mapping') == "/swagger-ui/**"

        List<Map<String, String>> swaggerSec = ctx.configuration.get('micronaut.security.intercept-url-map') as List<Map<String, String>>

        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/swagger/**" }
        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/swagger-ui/**" }

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=true"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=false"
        output["openapi.properties"].readLines()[3] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[4] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[5] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[6] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

}
