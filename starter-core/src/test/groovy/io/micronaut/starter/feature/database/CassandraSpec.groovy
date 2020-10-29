package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CassandraSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle cassandra feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cassandra'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.cassandra:micronaut-cassandra")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cassandra feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cassandra'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cassandra</groupId>
      <artifactId>micronaut-cassandra</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test cassandra configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cassandra'])

        then:
        commandContext.configuration.get('cassandra.default.clusterName'.toString()) == '"myCluster"'
        commandContext.configuration.get('cassandra.default.contactPoint'.toString()) == '"localhost"'
        commandContext.configuration.get('cassandra.default.port'.toString()) == 9042
        commandContext.configuration.get('cassandra.default.maxSchemaAgreementWaitSeconds'.toString()) == 20
        commandContext.configuration.get('cassandra.default.ssl'.toString()) == true
    }
}
