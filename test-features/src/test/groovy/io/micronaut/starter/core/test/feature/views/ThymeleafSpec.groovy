package io.micronaut.starter.core.test.feature.views

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class ThymeleafSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "thymeleafViews"
    }

    @Unroll
    void "test gradle views-thymeleaf runs fieldset suite"() {
        when:
        generateProject(Language.JAVA, BuildTool.GRADLE, ["views-thymeleaf", "views-fieldset-tck"])
        BuildResult result = executeGradle("build")

        then:
        result?.output?.contains("BUILD SUCCESS")
    }
}
