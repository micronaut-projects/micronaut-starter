package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Shared
import spock.lang.Subject

class LocalStackSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    LocalStack localStack = beanContext.getBean(LocalStack)

    void 'test readme.md with feature localstack contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate(['localstack'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.testcontainers.org/modules/localstack/")
    }

    void "test localstack belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == localStack.category
    }

    void 'test gradle localstack feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['localstack'] + (hasSqs ? ['jms-sqs'] : []))
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:testcontainers")')
        template.contains('testImplementation("org.testcontainers:testcontainers-localstack")')
        template.contains('testImplementation("com.amazonaws:aws-java-sdk-core")') == !hasSqs

        where:
        [language, hasSqs] << [LanguageUtils.JVM_LANGUAGES, [true, false]].combinations()
    }

    void 'test maven localstack feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['localstack'] + (hasSqs ? ['jms-sqs'] : []))
                .testFramework(TestFramework.JUNIT)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("org.testcontainers", "testcontainers", Scope.TEST)
        verifier.hasDependency("org.testcontainers", "testcontainers-localstack", Scope.TEST)
        verifier.hasDependency("com.amazonaws", "aws-java-sdk-core", Scope.TEST) == !hasSqs

        where:
        [language, hasSqs] << [supportedLanguages(BuildTool.MAVEN), [true, false]].combinations()
    }

}
