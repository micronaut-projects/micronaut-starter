package io.micronaut.starter.core.test.feature.agorapulse.slack

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import org.gradle.testkit.runner.BuildResult
import org.yaml.snakeyaml.Yaml
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

class SlackSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return 'agorapulse-micronaut-slack'
    }

    @Unroll
    void "test maven agorapulse-micronaut-slack with #language, #testFramework and #applicationType"(
            Language language,
            TestFramework testFramework,
            ApplicationType applicationType
    ) {
        when:
        generateProject(language, BuildTool.MAVEN, ['agorapulse-micronaut-slack'], applicationType, testFramework)
        String output = executeMaven('compile test')

        then:
        output?.contains('BUILD SUCCESS')

        where:
        [language, testFramework, applicationType] << [
                Language.values(),
                TestFramework.values(),
                [ApplicationType.DEFAULT],
        ].combinations()
    }

    @Unroll
    void "test gradle agorapulse-micronaut-slack with #language, #testFramework and #applicationType using #buildTool"(
            BuildTool buildTool,
            Language language,
            TestFramework testFramework,
            ApplicationType applicationType
    ) {
        when:
        generateProject(language, buildTool, ['agorapulse-micronaut-slack'], applicationType, testFramework)

        then:
        Files.exists(Paths.get(dir.path, 'slack-manifest.yml'))

        when:
        BuildResult result = executeGradle('test')

        then:
        result?.output?.contains('BUILD SUCCESS')

        where:
        [buildTool, language, testFramework, applicationType] << [
                [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN],
                Language.values(),
                TestFramework.values(),
                [ApplicationType.DEFAULT],
        ].combinations()
    }

    void "slack manifest generated"() {
        given:
        generateProject(Language.JAVA, BuildTool.GRADLE, ['agorapulse-micronaut-slack'], ApplicationType.DEFAULT, TestFramework.JUNIT)

        when:
        File slackManifestFile = new File(dir.path, 'slack-manifest.yml')

        then:
        slackManifestFile.exists()

        when:
        Map manifestContent = new Yaml().loadAs(slackManifestFile.newReader(), Map)

        then:
        manifestContent
        manifestContent.display_information
        manifestContent.display_information.name == 'Foo'
        manifestContent.features
        manifestContent.features.bot_user
        manifestContent.features.bot_user.display_name == 'Foo'
        manifestContent.features.slash_commands
        manifestContent.features.slash_commands[0].command == '/foo'
        manifestContent.features.slash_commands[0].url == 'https://example-micronaut-foo.loca.lt/slack/events'
        manifestContent.features.slash_commands[0].description == 'Foo'
        manifestContent.settings
        manifestContent.settings.interactivity
        manifestContent.settings.interactivity.is_enabled
        manifestContent.settings.interactivity.request_url == 'https://example-micronaut-foo.loca.lt/slack/events'
    }
}
