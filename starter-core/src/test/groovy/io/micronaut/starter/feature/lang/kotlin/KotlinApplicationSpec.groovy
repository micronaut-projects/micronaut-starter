package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Unroll

class KotlinApplicationSpec extends BeanContextSpec implements CommandOutputFixture {
    void 'Application file is generated for a default application type with gradle and referenced in build.gradle mainClassName for language: kotlin'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, TestFramework.KOTLINTEST, BuildTool.GRADLE),
                []
        )

        then:
        output.containsKey("src/main/kotlin/example/micronaut/Application.${Language.KOTLIN.extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('mainClassName = "example.micronaut.Application"')
        buildGradle.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")')
    }

    @Unroll
    void "for gradle and #language kotlin-runtime is not added"() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, null, BuildTool.GRADLE),
                []
        )
        def buildGradle = output['build.gradle']

        then:
        !buildGradle.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")')

        where:
        language << (Language.values().toList() - Language.KOTLIN)
    }

    @Unroll
    void "for maven and #language kotlin-runtime is not added"() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, null, BuildTool.MAVEN),
                []
        )
        def pom = output['pom.xml']

        then:
        !pom.contains("""
    <dependency>
      <groupId>io.micronaut.kotlin</groupId>
      <artifactId>micronaut-kotlin-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << (Language.values().toList() - Language.KOTLIN)
    }

    @Unroll
    void "for maven and kotlin kotlin-runtime is added"() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.KOTLIN, null, BuildTool.MAVEN),
                []
        )
        def pom = output['pom.xml']

        then:
        pom.contains("""
    <dependency>
      <groupId>io.micronaut.kotlin</groupId>
      <artifactId>micronaut-kotlin-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
