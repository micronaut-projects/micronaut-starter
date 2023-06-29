package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class SwaggerUISpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void "test swagger-ui features"() {
        when:
        Features features = getFeatures(['swagger-ui'])

        then:
        features.contains("openapi")
    }

    void "test config without security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['swagger-ui'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.swagger-ui.paths') == "classpath:META-INF/swagger/views/swagger-ui"
        ctx.configuration.get('micronaut.router.static-resources.swagger-ui.mapping') == "/swagger-ui/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=true"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[3] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[4] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[5] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

    void "test config with security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['swagger-ui', 'security'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.swagger-ui.paths') == "classpath:META-INF/swagger/views/swagger-ui"
        ctx.configuration.get('micronaut.router.static-resources.swagger-ui.mapping') == "/swagger-ui/**"

        List<Map<String, String>> swaggerSec = ctx.configuration.get('micronaut.security.intercept-url-map') as List<Map<String, String>>

        swaggerSec.get(0).get("access") == "isAnonymous()"
        swaggerSec.get(0).get("pattern") == "/swagger/**"

        swaggerSec.get(1).get("access") == "isAnonymous()"
        swaggerSec.get(1).get("pattern") == "/swagger-ui/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=true"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[3] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[4] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[5] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

}
