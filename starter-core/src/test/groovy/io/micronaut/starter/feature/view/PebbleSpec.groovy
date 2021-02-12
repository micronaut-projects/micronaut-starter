package io.micronaut.starter.feature.view

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleDsl
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class PebbleSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature views-pebble contains links to micronaut docs'() {
        when:
        def output = generate(['views-pebble'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://pebbletemplates.io/")
        readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#pebble")
    }

    @Unroll
    void 'test gradle views-pebble feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-pebble'], language), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut.views:micronaut-views-pebble")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven views-pebble feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['views-pebble'], language), new MavenBuild()).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.views</groupId>
      <artifactId>micronaut-views-pebble</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
