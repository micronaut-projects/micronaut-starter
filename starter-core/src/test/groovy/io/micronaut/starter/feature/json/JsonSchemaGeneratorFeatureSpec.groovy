package io.micronaut.starter.feature.json

import io.micronaut.core.util.StringUtils
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.StarterCoordinates
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

import static io.micronaut.starter.application.ApplicationType.DEFAULT
import static io.micronaut.starter.options.BuildTool.MAVEN

class JsonSchemaGeneratorFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    private static final String GRADLE_PLUGIN_VERSION = StarterCoordinates.MICRONAUT_GRADLE_PLUGIN.version
    private static final String JSONSCHEMA_PLUGIN = 'id("io.micronaut.jsonschema") version "' + GRADLE_PLUGIN_VERSION + '"'

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

    void 'application with #buildTool and feature json-schema-generator for language=#language'(BuildTool buildTool, Language language) {
        when:
        String output = build(buildTool, language)

        then:
        output.contains(JSONSCHEMA_PLUGIN)
        output.contains('jsonschema {')
        output.contains('fromUrl(')
        output.contains('outputPackageName.set("com.example.animals")')

        when:
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, output)

        then:
        verifier.hasBuildPlugin("io.micronaut.jsonschema")

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values().toList()].combinations()
    }

    @Unroll
    void 'application with maven and feature json-schema-generator for language=#language'() {
        when:
        String output = build(MAVEN, language)

        then:
        output.contains("<micronaut.jsonschema.generator.outputPackageName>com.example.animals</micronaut.jsonschema.generator.outputPackageName>")

        when:
        String expected = "<micronaut.jsonschema.generator.enabled>${StringUtils.TRUE}</micronaut.jsonschema.generator.enabled>"

        then:
        output.contains(expected)

        where:
        language << Language.values().toList()
    }

    void 'test json-schema-generator feature does NOT add jason-schema feature'() {
        when:
        GeneratorContext generatorContext = buildGeneratorContext([JsonSchemaGeneratorFeature.NAME])

        then:
        !generatorContext.hasFeature(JsonSchemaFeature)
    }

    private String build(BuildTool buildTool, Language language, List<String> features = [JsonSchemaGeneratorFeature.NAME]) {
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(DEFAULT)
                .features(features)
                .render()
    }
}
