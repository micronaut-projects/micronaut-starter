package io.micronaut.starter.feature.validator

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

class MicronautValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    MicronautValidationFeature micronautValidationFeature = beanContext.getBean(MicronautValidationFeature)

    void 'test readme.md with feature http-session contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['validation'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-validation/latest/guide/")
    }

    void "test Micronaut Validation belongs to Validation category"() {
        expect:
        Category.VALIDATION == micronautValidationFeature.category
    }

    void "test Micronaut Validation is visible"() {
        expect:
        micronautValidationFeature.visible
    }

    void "test Micronaut Validation supports application type=#appType"(ApplicationType appType) {
        expect:
        micronautValidationFeature.supports(appType)

        where:
        appType << ApplicationType.values().toList()
    }

    void 'test Micronaut Validation feature for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .language(language)
                .features(['validation'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.validation", "micronaut-validation-processor", Scope.ANNOTATION_PROCESSOR)
        if (language != Language.GROOVY) {
            assert verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE)
        }
        verifier.hasDependency("jakarta.validation", "validation-api", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
