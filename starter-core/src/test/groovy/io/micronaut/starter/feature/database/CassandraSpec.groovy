package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.micrometer.MicrometerFeature
import io.micronaut.starter.feature.testresources.TestResources
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class CassandraSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test cassandra feature dependency for language=#language and buildTool=#buildTool'(
            BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([Cassandra.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.cassandra","micronaut-cassandra", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'test cassandra micrometer feature=#feature for language=#language and buildTool=#buildTool'(
            BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([Cassandra.NAME, feature])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("com.datastax.oss","java-driver-metrics-micrometer", Scope.COMPILE)

        where:
        [language, buildTool, feature] << [Language.values(), BuildTool.values(), beanContext.getBeansOfType(MicrometerFeature)*.name].combinations()
    }

    void 'test cassandra configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([Cassandra.NAME])

        then:
        commandContext.configuration.get('cassandra.default.basic.contact-points') == ['localhost:9042']
        commandContext.configuration.get('cassandra.default.basic.load-balancing-policy.local-datacenter') == 'datacenter1'
        commandContext.configuration.get('cassandra.default.basic.session-name') == 'defaultSession'
        commandContext.configuration.get('cassandra.default.advanced.control-connection.schema-agreement.timeout') == 20
        commandContext.configuration.get('cassandra.default.advanced.ssl-engine-factory') == 'DefaultSslEngineFactory'
    }

    void 'test cassandra configuration for integration with #micrometerFeature'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([Cassandra.NAME, micrometerFeature])

        then:
        commandContext.configuration.get('micronaut.metrics.enabled') == true
        commandContext.configuration.get('cassandra.default.advanced.metrics.factory.class') == 'MicrometerMetricsFactory'
        commandContext.configuration.get('cassandra.default.advanced.metrics.session.enabled') == ['connected-nodes', 'cql-requests', 'bytes-sent', 'bytes-received']
        commandContext.configuration.get('cassandra.default.advanced.metrics.node.enabled') == ['cql-requests', 'bytes-sent', 'bytes-received']

        where:
        micrometerFeature << beanContext.getBeansOfType(MicrometerFeature)*.name
    }

    void 'test cassandra configuration for integration with test-resources'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([Cassandra.NAME, TestResources.NAME])

        then:
        commandContext.configuration.get('cassandra.default.basic.contact-points') == ['localhost:${cassandra.port}']
        commandContext.configuration.get('test-resources.containers.cassandra.startup-timeout') == "600s"
        commandContext.configuration.get('test-resources.containers.cassandra.image-name') == 'cassandra'
        commandContext.configuration.get('test-resources.containers.cassandra.exposed-ports')[0]['cassandra.port'] == 9042
    }

    void 'test readme has docs'() {
        when:
        Map<String, String> output = generate([Cassandra.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://micronaut-projects.github.io/micronaut-cassandra/latest/guide/index.html')
        readme.contains('https://docs.datastax.com/en/developer/java-driver/latest/')
    }
}
