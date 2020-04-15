package io.micronaut.starter.feature.cassandra

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

class CassandraSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle cassandra feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['cassandra'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-cassandra"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven cassandra feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['cassandra'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-cassandra</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test cassandra configuration'() {
        when:
        CommandContext commandContext = buildCommandContext(['cassandra'])

        then:
        commandContext.configuration.get('cassandra.default.clusterName'.toString()) == '"myCluster"'
        commandContext.configuration.get('cassandra.default.contactPoint'.toString()) == '"localhost"'
        commandContext.configuration.get('cassandra.default.port'.toString()) == 9042
        commandContext.configuration.get('cassandra.default.maxSchemaAgreementWaitSeconds'.toString()) == 20
        commandContext.configuration.get('cassandra.default.ssl'.toString()) == true
    }
}
