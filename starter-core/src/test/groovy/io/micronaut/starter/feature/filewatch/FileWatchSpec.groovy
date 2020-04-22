package io.micronaut.starter.feature.filewatch

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.VersionInfo
import spock.lang.Requires
import spock.lang.Unroll

class FileWatchSpec extends BeanContextSpec {

    @Requires({ os.macOs })
    @Unroll
    void 'test gradle file-watch feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['file-watch'], language)).render().toString()

        then:
        template.contains("developmentOnly(\"io.micronaut:micronaut-runtime-osx:${VersionInfo.micronautVersion}\")")

        where:
        language << Language.values().toList()
    }

    @Requires({ os.macOs })
    @Unroll
    void 'test maven file-watch feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['file-watch'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-runtime-osx</artifactId>
      <version>${VersionInfo.micronautVersion}</version>
      <scope>provided</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test file-watch configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['file-watch'])

        then:
        commandContext.configuration.get('micronaut.io.watch.paths'.toString()) == 'src/main'
        commandContext.configuration.get('micronaut.io.watch.restart'.toString()) == true
    }
}
