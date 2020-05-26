package io.micronaut.starter.feature.lang.kotlin

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.fixture.CommandOutputFixture

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
        buildGradle.contains('mainClassName = "example.micronaut.ApplicationKt"')
    }
}
