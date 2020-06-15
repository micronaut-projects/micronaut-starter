package io.micronaut.starter.feature.reloading.filewatch

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.VersionInfo
import spock.lang.Requires
import spock.lang.Unroll

class FileWatchSpec extends BeanContextSpec {

    @Requires({ os.macOs })
    @Unroll
    void 'test gradle file-watch feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([], language)).render().toString()

        then:
        template.contains('developmentOnly("io.micronaut:micronaut-runtime-osx:$micronautVersion")')

        where:
        language << Language.values().toList()
    }

}
