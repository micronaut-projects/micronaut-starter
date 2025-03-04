package io.micronaut.starter.feature.other

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class RedocSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test redoc features"() {
        when:
        Features features = getFeatures(['redoc'])

        then:
        features.contains("openapi")
    }

    void "test config without security feature"() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().features(['redoc']).build())
        Properties properties = new Properties()
        properties.load(new StringReader(output['src/main/resources/application.properties']))

        then:
        properties.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        properties.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        properties.get('micronaut.router.static-resources.redoc.paths') == "classpath:META-INF/swagger/views/redoc"
        properties.get('micronaut.router.static-resources.redoc.mapping') == "/redoc/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=true"
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
        Map<String, String> output = generate(MicronautOptions.builder().features(['redoc', 'security']).build())
        Properties properties = new Properties()
        properties.load(new StringReader(output['src/main/resources/application.properties']))

        then:
        properties.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        properties.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        properties.get('micronaut.router.static-resources.redoc.paths') == "classpath:META-INF/swagger/views/redoc"
        properties.get('micronaut.router.static-resources.redoc.mapping') == "/redoc/**"

        List<Map<String, String>> swaggerSec = properties.get('micronaut.security.intercept-url-map') as List<Map<String, String>>

        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/swagger/**" }
        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/redoc/**" }

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=true"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=false"
        output["openapi.properties"].readLines()[3] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[4] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[5] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[6] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

    void "test redoc has third party docs"() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().features(['redoc']).build())

        then:
        output["README.md"].contains("https://github.com/Redocly/redoc#generate-beautiful-api-documentation-from-openapi")
    }

    void "test redoc has Micronaut docs"() {
        when:
        Map<String, String> output = generate(MicronautOptions.builder().features(['redoc']).build())

        then:
        output["README.md"].contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#redoc")
    }

}
