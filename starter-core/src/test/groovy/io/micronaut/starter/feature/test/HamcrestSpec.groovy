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

class HamcrestSpec  extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Hamcrest hamcrest = beanContext.getBean(Hamcrest)

    void 'test readme.md with feature hamcrest contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate(['hamcrest'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://hamcrest.org/JavaHamcrest/")
    }

    void "test hamcrest belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == hamcrest.category
    }

    @Unroll
    void 'test gradle hamcrest feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['hamcrest'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.hamcrest:hamcrest")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle hamcrest feature fails for language=#language when test framework is not Junit'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['hamcrest'])
                .language(language)
                .testFramework(testfw)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("hamcrest requires JUnit.")

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
    void 'test maven hamcrest feature fails for language=#language when test framework is not Junit'() {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['hamcrest'])
                .language(language)
                .testFramework(testfw)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("hamcrest requires JUnit.")

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
    void 'test maven hamcrest feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['hamcrest'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.hamcrest</groupId>
      <artifactId>hamcrest</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
