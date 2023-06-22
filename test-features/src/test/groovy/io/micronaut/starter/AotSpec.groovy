package io.micronaut.starter


import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.feature.build.MicronautAot
import io.micronaut.starter.feature.security.SecurityJWT
import io.micronaut.starter.feature.security.SecurityOAuth2
import io.micronaut.starter.feature.validator.MicronautValidationFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Issue

class AotSpec extends CommandSpec {
    def 'test aot with gradle'(BuildTool buildTool) {
        when:
        generateProject(
                Language.KOTLIN,
                buildTool,
                [MicronautAot.FEATURE_NAME_AOT, SecurityJWT.NAME, SecurityOAuth2.NAME]
        )
        BuildResult result = executeGradle("classes")

        then:
        result.output.contains("BUILD SUCCESS")

        where:
        buildTool << [BuildTool.GRADLE_KOTLIN, BuildTool.GRADLE]
    }

    @Override
    String getTempDirectoryPrefix() {
        return "kotlin-aop"
    }
}
