package io.micronaut.starter.core.test.feature.aop


import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Issue

class KotlinAopSpec extends CommandSpec {
    @Issue('https://github.com/micronaut-projects/micronaut-core/issues/9426')
    def 'test all-open plugin'() {
        when:
        generateProject(
                Language.KOTLIN,
                BuildTool.GRADLE_KOTLIN,
                [AOP.NAME, MicronautValidationFeature.NAME]
        )
        new File(dir, "src/main/kotlin/example/micronaut/TestController.kt").write("""
package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated

@Validated
@Controller
class TestController {
    @Get
    fun test() {
    }
}
""")
        BuildResult result = executeGradle("classes")

        then:
        result.output.contains("BUILD SUCCESS")
    }

    @Override
    String getTempDirectoryPrefix() {
        return "kotlin-aop"
    }
}
