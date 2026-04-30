package io.micronaut.starter.core.test.feature.agorapulse.permissions

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildTestFrameworkCombinations
import io.micronaut.starter.test.TestFrameworkCombinations
import io.micronaut.starter.util.LanguageUtils
import org.gradle.testkit.runner.BuildResult
import spock.lang.Ignore
import spock.lang.Unroll

@Ignore("agora community features do not support Micronaut Framework 4 yet")
class PermissionsSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return 'agorapulse-micronaut-permissions'
    }

    @Unroll
    void "test maven agorapulse-micronaut-permissions with #language, #testFramework and #applicationType"(
            Language language,
            ApplicationType applicationType,
            TestFramework testFramework
    ) {
        when:
        generateProject(language, BuildTool.MAVEN, ['agorapulse-micronaut-permissions'], applicationType, testFramework)
        String output = executeMaven('compile test')

        then:
        output?.contains('BUILD SUCCESS')

        where:
        [language, applicationType, testFramework] << [
                LanguageUtils.supportedLanguages(BuildTool.MAVEN),
                ApplicationType.values(),
                TestFrameworkCombinations.values(),
        ].combinations().findAll {
            return LanguageBuildTestFrameworkCombinations.filterByTestFramework(it)
        }
    }

    @Unroll
    void "test gradle agorapulse-micronaut-permissions with #language, #testFramework and #applicationType using #buildTool"(
            ApplicationType applicationType,
            Language language,
            BuildTool buildTool,
            TestFramework testFramework
    ) {
        when:
        generateProject(language, buildTool, ['agorapulse-micronaut-permissions'], applicationType, testFramework)
        BuildResult result = executeGradle('test')

        then:
        result?.output?.contains('BUILD SUCCESS')

        where:
        [applicationType, language, buildTool, testFramework] << [
                ApplicationType.values(),
                Language.values(),
                BuildTool.valuesGradle(),
                TestFrameworkCombinations.values(),
        ].combinations().findAll {
            return LanguageBuildTestFrameworkCombinations.filterByTestFramework(it)
        }
    }
}
