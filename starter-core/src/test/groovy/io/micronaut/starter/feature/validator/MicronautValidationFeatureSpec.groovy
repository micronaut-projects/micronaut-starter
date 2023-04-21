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
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Subject

class MicronautValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    MicronautValidationFeature micronautValidationFeature = beanContext.getBean(MicronautValidationFeature)

    @PendingFeature(reason = "Only Micronaut Framework 4 has docs")
    void 'test readme.md with feature http-session contains links to micronaut docs'() {
        when:
        def output = generate(['micronaut-validation'])
        def readme = output["README.md"]

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
                .features(['micronaut-validation'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        assert verifier.hasDependency("io.micronaut", "micronaut-validation", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }

    @PendingFeature(reason = "Only Micronaut Framework 4 has new dependency coordinates and processor")
    void 'test Micronaut Validation feature for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .language(language)
                .features(['micronaut-validation'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        assert verifier.hasAnnotationProcessor("io.micronaut.validation", "micronaut-validation-processor")
        assert verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE)
        assert verifier.hasDependency("jakarta.validation", "validation-api", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }
}
