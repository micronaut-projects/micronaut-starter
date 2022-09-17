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

class MockitoSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Mockito mockito = beanContext.getBean(Mockito)

    void 'test readme.md with feature mockito contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate(['mockito'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://site.mockito.org")
    }

    void "test mockito belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == mockito.category
    }

    @Unroll
    void 'test gradle mockito feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['mockito'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.mockito:mockito-core")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle mockito feature fails for language=#language when test framework is not Junit'(Language language, TestFramework testfw) {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['mockito'])
                .testFramework(testfw)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("Mockito requires JUnit.")

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
    void 'test maven mockito feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['mockito'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}