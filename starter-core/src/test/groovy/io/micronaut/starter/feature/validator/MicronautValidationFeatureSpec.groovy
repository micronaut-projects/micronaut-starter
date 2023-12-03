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
import io.micronaut.starter.options.Options
import spock.lang.Issue
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

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1914")
    void 'jakarta.validation:jakarta.validation-api for gradle should not include version'() {
        when:
        Map<String, String> output = generate(['validation'])
        String build = output["build.gradle.kts"]

        then:
        build
        build.contains('implementation("jakarta.validation:jakarta.validation-api")')
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/1914")
    void 'jakarta.validation:jakarta.validation-api for maven should not include version'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN),['validation'])
        String pom = output["pom.xml"]

        then:
        pom
        pom.contains('''
    <dependency>
      <groupId>jakarta.validation</groupId>
      <artifactId>jakarta.validation-api</artifactId>
      <scope>compile</scope>
    </dependency>''')
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
        verifier.hasAnnotationProcessor("io.micronaut.validation", "micronaut-validation-processor")
        if (language != Language.GROOVY) {
            assert verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE)
        }
        verifier.hasDependency("jakarta.validation", "jakarta.validation-api", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
