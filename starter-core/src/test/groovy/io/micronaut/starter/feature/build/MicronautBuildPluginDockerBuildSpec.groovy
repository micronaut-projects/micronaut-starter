package io.micronaut.starter.feature.build

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class MicronautBuildPluginDockerBuildSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + ['spec.name': 'MicronautBuildPluginDockerBuildSpec']
    }

    @Unroll
    void 'it is possible to generate an application type with gradle and dockerBuild extension for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, BuildTool.GRADLE),
                []
        )
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('id("io.micronaut.application")')
        buildGradle.contains('''\
dockerBuild {
    images = [
        "gcr.io/micronaut-guides/micronautguide:latest"
    ]
}''')
        where:
        language << Language.values().toList()
        extension << Language.extensions()
    }
}
