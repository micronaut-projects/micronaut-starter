package io.micronaut.starter.feature.lang

import io.micronaut.projectgen.micronaut.MicronautOptions
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
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.GROOVY)
                .testFramework(TestFramework.SPOCK)
                .buildTool(BuildTool.GRADLE)
                .build()
        when:
        def output = generate(options)

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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.GROOVY)
                .testFramework(TestFramework.SPOCK)
                .buildTool(build)
                .build()
        def output = generate(options)

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
