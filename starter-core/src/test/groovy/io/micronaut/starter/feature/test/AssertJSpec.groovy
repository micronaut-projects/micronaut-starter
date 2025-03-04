package io.micronaut.starter.feature.test

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.ContextFactory
import io.micronaut.starter.application.DefaultAvailableFeatures
import io.micronaut.starter.feature.Category
import io.micronaut.projectgen.core.feature.FeatureContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class AssertJSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AssertJ assertj = beanContext.getBean(AssertJ)

    void 'test readme.md with feature assertJ contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate(['assertj'])
        String readme = output["README.md"]

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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .buildTool(BuildTool.GRADLE_KOTLIN)
                .javaVersion(JdkVersion.JDK_17)
                .features(["assertj"])
                .build()

        when:
        FeatureContext featureContext = contextFactory.createFeatureContext(
                beanContext.getBean(DefaultAvailableFeatures),
                options
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