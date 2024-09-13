package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

class JsonSchemaValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    JsonSchemaValidationFeature jsonSchemaValidationFeature = beanContext.getBean(JsonSchemaValidationFeature)

    void 'readme.md with feature json-schema-validation contains links to docs'() {
        when:
        Map<String, String> output = generate([JsonSchemaFeature.NAME, JsonSchemaValidationFeature.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/index.html#validation")
    }

    void "json-schema-validation belongs to API category"() {
        expect:
        Category.API == jsonSchemaValidationFeature.category
    }

    void "json-schema-validation supports application type = #applicationType"(ApplicationType applicationType) {
        expect:
        jsonSchemaValidationFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "json-schema-validation feature adds test dependency for language=#language buildTool=#buildTool "(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JsonSchemaFeature.NAME, JsonSchemaValidationFeature.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template
        verifier.hasDependency("io.micronaut.jsonschema", "micronaut-json-schema-validation", Scope.TEST)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }

    void 'test json-schema-validation feature adds jason-schema feature'() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([JsonSchemaValidationFeature.NAME])

        then:
        generatorContext.hasFeature(JsonSchemaFeature)
    }
}
