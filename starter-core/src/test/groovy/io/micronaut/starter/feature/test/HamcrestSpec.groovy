package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleDsl
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class HamcrestSpec  extends BeanContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Hamcrest hamcrest = beanContext.getBean(Hamcrest)

    void 'test readme.md with feature hamcrest contains links to 3rd party docs'() {
        when:
        def output = generate(['hamcrest'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("http://hamcrest.org/JavaHamcrest/")
    }

    void "test hamcrest belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == hamcrest.category
    }

    @Unroll
    void 'test gradle hamcrest feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['hamcrest'],
                language, TestFramework.JUNIT), new GradleBuild()).render().toString()

        then:
        template.contains('testImplementation("org.hamcrest:hamcrest")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle hamcrest feature fails for language=#language when test framework is not Junit'() {
        when:
        buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['hamcrest'], language, testfw),
                new GradleBuild()).render().toString()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("Hamcrest requires JUnit.")

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
        pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['hamcrest'], language,
                testfw), []).render().toString()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("Hamcrest requires JUnit.")

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
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['hamcrest'], language,
                TestFramework.JUNIT), new MavenBuild()).render().toString()

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