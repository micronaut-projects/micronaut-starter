package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class JunitParamsSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    JunitParams junitParams = beanContext.getBean(JunitParams)

    void 'test readme.md with feature junit-params contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate(['junit-params'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests")
    }

    void "test junit-params belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == junitParams.category
    }

    @Unroll
    void 'test gradle junit-params feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['junit-params'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.junit.jupiter:junit-jupiter-params:')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle junit-params feature fails for language=#language when test framework is not Junit'(Language language, TestFramework testfw) {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['junit-params'])
                .testFramework(testfw)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("junit-params requires JUnit.")

        where:
        language        | testfw
        Language.JAVA   | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.SPOCK
        Language.GROOVY | TestFramework.SPOCK
        Language.JAVA   | TestFramework.KOTEST
        Language.KOTLIN | TestFramework.KOTEST
        Language.GROOVY | TestFramework.KOTEST
    }

    @Unroll
    void 'test maven junit-params feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['junit-params'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
""")

        where:
        language << Language.values().toList()
    }
}