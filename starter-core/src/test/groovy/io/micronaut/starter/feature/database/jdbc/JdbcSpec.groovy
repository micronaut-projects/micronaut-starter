package io.micronaut.starter.feature.database.jdbc

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class JdbcSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle jdbc feature #jdbcFeature'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([jdbcFeature])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-${jdbcFeature}\"")

        where:
        jdbcFeature << ["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"]
    }

    @Unroll
    void 'test maven jdbc feature #jdbcFeature'() {
        when:
        String template = pom.template(buildProject(), getFeatures([jdbcFeature]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-${jdbcFeature}</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(buildProject(), getFeatures([jdbcFeature], Language.kotlin), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-${jdbcFeature}</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        when:
        template = pom.template(buildProject(), getFeatures([jdbcFeature], Language.groovy), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-${jdbcFeature}</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        jdbcFeature << ["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"]
    }

    void 'test there can only be one jdbc feature'() {
        when:
        getFeatures(["jdbc-dbcp", "jdbc-hikari", "jdbc-tomcat"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}
