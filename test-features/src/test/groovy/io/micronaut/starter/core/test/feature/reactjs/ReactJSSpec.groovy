package io.micronaut.starter.core.test.feature.reactjs

import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.PendingFeature

class ReactJSSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        return "react"
    }

    @PendingFeature
    void "test react in #language with #buildTool"(BuildTool buildTool, Language language) {
        when:
        generateProject(language, buildTool, [Yaml.NAME, 'views-react'])
        String output = executeGradle("--info", "--stacktrace", "build")?.output

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [buildTool, language] << [
                BuildTool.valuesGradle(),
                [Language.JAVA, Language.KOTLIN]
        ].combinations()
    }

    // TODO: Implement a browser-based test that verifies the JS hydrates correctly end-to-end.
}