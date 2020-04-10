package io.micronaut.starter.feature.migration

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class LiquibaseSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test the dependency is added to the gradle build"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['liquibase'])).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-liquibase"')
    }

    void "test the dependency is added to the maven build"() {
        when:
        String template = pom.template(buildProject(), getFeatures(['liquibase']), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-liquibase</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
