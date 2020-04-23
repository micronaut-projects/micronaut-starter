package io.micronaut.starter.feature.lang

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class GroovyApplicationSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void "test generated Groovy application for build tool - #build"() {
        given:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.GROOVY, TestFramework.SPOCK, build))

        expect:
        output[build.fileName].contains(dependency)

        where:
        build            | dependency
        BuildTool.MAVEN  | """<dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject-groovy</artifactId>
      <scope>provided</scope>
    </dependency>"""
        BuildTool.GRADLE | 'compileOnly("io.micronaut:micronaut-inject-groovy")'
    }
}
