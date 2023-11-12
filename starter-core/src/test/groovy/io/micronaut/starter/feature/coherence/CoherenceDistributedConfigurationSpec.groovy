package io.micronaut.starter.feature.coherence

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceDistributedConfigurationSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature coherence-distributed-configuration contains links to micronaut docs'() {
        when:
        def output = generate([CoherenceDistributedConfiguration.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#distributedConfiguration")
        readme.contains("https://coherence.java.net/")
    }

    void 'test configuration with feature coherence-distributed-configuration'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, CoherenceDistributedConfiguration.NAME])
        String configuration = output['src/main/resources/bootstrap.yml']

        then:
        configuration
        configuration.contains("""
coherence:
  client:
    enabled: true
    host: \${COHERENCE_HOST:localhost}
    port: \${COHERENCE_PORT:1408}
""")
    }

    @Unroll
    void 'test gradle coherence-distributed-configuration feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([CoherenceDistributedConfiguration.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-distributed-configuration")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven coherence-distributed-configuration feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([CoherenceDistributedConfiguration.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.coherence</groupId>
      <artifactId>micronaut-coherence</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>com.oracle.coherence.ce</groupId>
      <artifactId>coherence</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.coherence</groupId>
      <artifactId>micronaut-coherence-distributed-configuration</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
