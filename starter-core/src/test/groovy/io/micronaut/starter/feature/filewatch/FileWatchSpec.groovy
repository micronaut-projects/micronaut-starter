package io.micronaut.starter.feature.filewatch

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.VersionInfo
import spock.lang.AutoCleanup
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class FileWatchSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Requires({ os.macOs })
    @Unroll
    void 'test gradle file-watch feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['file-watch'], language)).render().toString()

        then:
        template.contains("developmentOnly \"io.micronaut:micronaut-runtime-osx:${VersionInfo.version}\"")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
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
      <version>${VersionInfo.version}</version>
      <scope>provided</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test file-watch configuration'() {
        when:
        CommandContext commandContext = buildCommandContext(['file-watch'])

        then:
        commandContext.configuration.get('micronaut.io.watch.paths'.toString()) == 'src/main'
        commandContext.configuration.get('micronaut.io.watch.restart'.toString()) == true
    }
}
