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

class JsonSchemaFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    JsonSchemaFeature jsonSchemaFeature = beanContext.getBean(JsonSchemaFeature)

    void 'readme.md with feature json-schema contains links to docs'() {
        when:
        Map<String, String> output = generate([JsonSchemaFeature.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/")
        readme.contains("https://json-schema.org/learn/getting-started-step-by-step")
    }

    void 'test json-schema configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([JsonSchemaFeature.NAME])

        then:
        commandContext.configuration.get('micronaut.router.static-resources.jsonschema.paths'.toString()) == 'classpath:META-INF/schemas'
        commandContext.configuration.get('micronaut.router.static-resources.jsonschema.mapping'.toString()) == '/schemas/**'
    }


    void "json-schema belongs to API category"() {
        expect:
        Category.API == jsonSchemaFeature.category
    }

    void "json-schema supports application type = #applicationType"(ApplicationType applicationType) {
        expect:
        jsonSchemaFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "json-schema feature adds dependencies for language=#language buildTool=#buildTool "(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JsonSchemaFeature.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template
        verifier.hasAnnotationProcessor("io.micronaut.jsonschema", "micronaut-json-schema-processor")
        verifier.hasDependency("io.micronaut.jsonschema", "micronaut-json-schema-annotations")

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }
}
