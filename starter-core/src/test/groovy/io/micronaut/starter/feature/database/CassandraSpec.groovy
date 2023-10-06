package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.micrometer.MicrometerFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CassandraSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle cassandra feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cassandra'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cassandra:micronaut-cassandra")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cassandra feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cassandra'])
                .render()

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

    void 'test cassandra configuration for integration with #micrometerFeature'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cassandra', micrometerFeature])

        then:
        commandContext.configuration.get('micronaut.metrics.enabled'.toString()) == true
        commandContext.configuration.get('cassandra.default.advanced.metrics.factory.class'.toString()) == 'MicrometerMetricsFactory'
        commandContext.configuration.get('cassandra.default.advanced.metrics.session.enabled'.toString()) == '[connected-nodes, cql-requests, bytes-sent, bytes-received]'
        commandContext.configuration.get('cassandra.default.advanced.metrics.node.enabled'.toString()) == '[cql-requests, bytes-sent, bytes-received]'

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature)*.name

    }
}
