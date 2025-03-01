package io.micronaut.starter.feature.lang

import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Unroll

class GroovyApplicationSpec extends BeanContextSpec implements CommandOutputFixture {
    void 'Application file is generated for a default application type with gradle and referenced in build.gradle mainClassName for language: groovy'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.GROOVY, TestFramework.SPOCK, BuildTool.GRADLE),
                []
        )

        then:
        output.containsKey("src/main/groovy/example/micronaut/Application.${Language.GROOVY.extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('mainClass = "example.micronaut.Application"')
    }

    @Unroll
    void "test generated Groovy application for build tool - #build"() {
        given:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.GROOVY, TestFramework.SPOCK, build))

        expect:
        output[build.buildFileName].contains(dependency)

        where:
        build            | dependency
        BuildTool.MAVEN  | """<dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject-groovy</artifactId>
      <scope>provided</scope>
    </dependency>"""
    }
}
