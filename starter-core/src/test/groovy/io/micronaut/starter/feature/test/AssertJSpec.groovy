package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AssertJSpec extends BeanContextSpec implements CommandOutputFixture {
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

    @Unroll
    void 'test gradle assertj feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['assertj'],
                language, TestFramework.JUNIT), false).render().toString()

        then:
        template.contains('testImplementation("org.assertj:assertj-core")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle assertj feature fails for language=#language when test framework is not Junit'() {
        when:
        buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['assertj'], language, testfw),
                false).render().toString()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("AssertJ requires JUnit.")

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
    void 'test maven assertj feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['assertj'], language,
                TestFramework.JUNIT), []).render().toString()

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