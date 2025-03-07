package io.micronaut.starter.feature.json

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

class JsonSchemaGeneratorFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    JsonSchemaGeneratorFeature jsonSchemaGeneratorFeature = beanContext.getBean(JsonSchemaGeneratorFeature)

    void 'readme.md with feature json-schema-generator contains links to docs'() {
        when:
        Map<String, String> output = generate([JsonSchemaGeneratorFeature.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/")
    }

    void "json-schema-generator belongs to API category"() {
        expect:
        Category.API == jsonSchemaGeneratorFeature.category
    }

    void "json-schema-generator supports application type = #applicationType"(ApplicationType applicationType) {
        expect:
        jsonSchemaGeneratorFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "json-schema-generator feature adds dependencies for language=#language buildTool=#buildTool "(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JsonSchemaGeneratorFeature.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template
        verifier.hasDependency("io.micronaut.jsonschema", "micronaut-json-schema-generator")
        verifier.hasDependency("io.micronaut.jsonschema", "micronaut-json-schema-annotations")

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }

    void 'test json-schema-generator feature does NOT add jason-schema feature'() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([JsonSchemaGeneratorFeature.NAME])

        then:
        !generatorContext.hasFeature(JsonSchemaFeature)
    }
}
