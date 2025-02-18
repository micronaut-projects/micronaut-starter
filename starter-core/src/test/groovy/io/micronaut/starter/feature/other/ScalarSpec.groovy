package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class ScalarSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test scalar features"() {
        when:
        Features features = getFeatures(['scalar'])

        then:
        features.contains("openapi")
    }

    void "test config without security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['scalar'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.scalar.paths') == "classpath:META-INF/swagger/views/scalar"
        ctx.configuration.get('micronaut.router.static-resources.scalar.mapping') == "/scalar/**"

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=false"
        output["openapi.properties"].readLines()[3] == "scalar.enabled=true"
        output["openapi.properties"].readLines()[4] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[5] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[6] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[7] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")
    }

    void "test config with security feature"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['scalar', 'security'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        ctx.configuration.get('micronaut.router.static-resources.swagger.paths') == "classpath:META-INF/swagger"
        ctx.configuration.get('micronaut.router.static-resources.swagger.mapping') == "/swagger/**"
        ctx.configuration.get('micronaut.router.static-resources.scalar.paths') == "classpath:META-INF/swagger/views/scalar"
        ctx.configuration.get('micronaut.router.static-resources.scalar.mapping') == "/scalar/**"

        List<Map<String, String>> swaggerSec = ctx.configuration.get('micronaut.security.intercept-url-map') as List<Map<String, String>>

        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/swagger/**" }
        swaggerSec.any { it.access == "isAnonymous()" && it.pattern == "/scalar/**" }

        output["openapi.properties"].readLines()[0] == "swagger-ui.enabled=false"
        output["openapi.properties"].readLines()[1] == "redoc.enabled=false"
        output["openapi.properties"].readLines()[2] == "openapi-explorer.enabled=false"
        output["openapi.properties"].readLines()[3] == "scalar.enabled=true"
        output["openapi.properties"].readLines()[4] == "rapidoc.enabled=false"
        output["openapi.properties"].readLines()[5] == "rapidoc.bg-color=#14191f"
        output["openapi.properties"].readLines()[6] == "rapidoc.text-color=#aec2e0"
        output["openapi.properties"].readLines()[7] == "rapidoc.sort-endpoints-by=method"

        output.containsKey("src/main/java/example/micronaut/FooController.java")
        output.containsKey("src/test/java/example/micronaut/FooTest.java")

    }

    void "test scalar has third party docs"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['scalar'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        output["README.md"].contains("https://github.com/scalar/scalar")
    }

    void "test scalar has Micronaut docs"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['scalar'])
        def output = generate(ApplicationType.DEFAULT, ctx)

        then:
        output["README.md"].contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#scalar")
    }

}
