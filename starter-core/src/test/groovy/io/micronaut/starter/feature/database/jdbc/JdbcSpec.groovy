package io.micronaut.starter.feature.database.jdbc

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
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
    void "test gradle jdbc feature #jdbcFeature"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([jdbcFeature])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-${jdbcFeature}\"")
        template.contains("runtimeOnly \"com.h2database:h2\"")

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }

    @Unroll
    void "test maven jdbc feature #jdbcFeature"() {
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
        template.contains("""
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }

    void "test there can only be one jdbc feature"() {
        when:
        getFeatures(beanContext.getBeansOfType(JdbcFeature)*.name)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

    void "test jdbc feature configuration"() {
        when:
        CommandContext ctx = buildCommandContext([jdbcFeature])
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ctx.getTemplates().get("yamlConfig").write(baos)

        then:
        then:
        baos.toString().contains("""
datasources.default: {
  }  
""".trim())

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }
}
