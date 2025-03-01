package io.micronaut.starter.feature.build

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import spock.lang.Unroll

class MicronautBuildPluginDockerBuildNativeSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + ['spec.name': 'MicronautBuildPluginDockerBuildNativeSpec']
    }

    @Unroll
    void 'it is possible to generate an application type with gradle and dockerBuild extension for language: #language'(Language language, String extension) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                MicronautOptions.builder().language(language).buildTool(BuildTool.GRADLE).build(),
                []
        )
        String buildGradle = output['build.gradle']

        then:
        buildGradle.contains('id("io.micronaut.application")')
        buildGradle.contains('''\
dockerBuildNative {
    images = [
        "gcr.io/micronaut-guides/micronautguide:latest"
    ]
}''')
        where:
        language << Language.values().toList()
        extension << Language.extensions()
    }
}
