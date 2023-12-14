package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.ContextFactory
import io.micronaut.starter.application.DefaultAvailableFeatures
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.FeatureContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class AssertJSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AssertJ assertj = beanContext.getBean(AssertJ)

    void 'test readme.md with feature assertJ contains links to 3rd party docs'() {
        when:
        def output = generate(['assertj'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://assertj.github.io/doc/")
    }

    void "test assertj belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == assertj.category
    }

    void 'test gradle assertj feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['assertj'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.assertj:assertj-core")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle assertj succeeds for defaults as JUnit is automatically selected'() {
        given:
        ContextFactory contextFactory = beanContext.getBean(ContextFactory)

        when:
        FeatureContext featureContext = contextFactory.createFeatureContext(
                beanContext.getBean(DefaultAvailableFeatures),
                ["assertj"],
                ApplicationType.DEFAULT,
                new Options(Language.JAVA, null, BuildTool.GRADLE_KOTLIN, JdkVersion.JDK_17),
                null
        )

        then:
        noExceptionThrown()

        when:
        contextFactory.createGeneratorContext(
                null, featureContext, ConsoleOutput.NOOP
        )

        then:
        noExceptionThrown()
    }

    void 'test gradle assertj feature fails for language=#language when test framework is not Junit'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['assertj'])
                .language(language)
                .testFramework(testfw)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("assertj requires JUnit.")

        where:
        language        | testfw
        Language.JAVA   | TestFramework.SPOCK
        Language.KOTLIN | TestFramework.SPOCK
        Language.GROOVY | TestFramework.SPOCK
        Language.JAVA   | TestFramework.KOTEST
        Language.KOTLIN | TestFramework.KOTEST
        Language.GROOVY | TestFramework.KOTEST
    }

    void 'test maven assertj feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['assertj'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-core</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}