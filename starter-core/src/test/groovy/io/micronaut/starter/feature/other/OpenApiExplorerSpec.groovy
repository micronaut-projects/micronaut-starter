package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class OpenApiExplorerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test openapi-explorer features"() {
        when:
        Features features = getFeatures(['openapi-explorer'])

        then:
        features.contains("openapi")
    }

    void "test config without security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['openapi-explorer'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.openapi-explorer.paths') == "classpath:META-INF/swagger/views/openapi-explorer"
        ctx.configuration.get('micronaut.router.static-resources.openapi-explorer.mapping') == "/openapi-explorer/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=true"
        output["openapi.properties"].readLines()[3] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[4] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[5] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[6] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")
    }

    void "test config with security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['openapi-explorer', 'security'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.openapi-explorer.paths') == "classpath:META-INF/swagger/views/openapi-explorer"
        ctx.configuration.get('micronaut.router.static-resources.openapi-explorer.mapping') == "/openapi-explorer/**"

        List<Map<String, String>> swaggerSec = ctx.configuration.get('micronaut.security.intercept-url-map') as List<Map<String, String>>

        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/swagger/**" }
        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/openapi-explorer/**" }

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=true"
        output["openapi.properties"].readLines()[3] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[4] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[5] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[6] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

    void "test openapi-explorer has third party docs"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['openapi-explorer'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        output["README.md"].contains("https://github.com/Authress-Engineering/openapi-explorer")
    }

    void "test openapi-explorer has Micronaut docs"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['openapi-explorer'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        output["README.md"].contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#openapiExplorer")
    }

}
