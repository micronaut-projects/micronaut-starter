package io.micronaut.starter.feature.coherence

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature
import spock.lang.Unroll

class CoherenceDataSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature coherence-data contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([CoherenceData.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#repository")
        readme.contains("https://coherence.java.net/")
    }

    @PendingFeature(reason = "missing example")
    void 'test configuration with feature coherence-data'() {
        when:
        def output = generate([Yaml.NAME, CoherenceData.NAME])
        def configuration = output['src/main/resources/application.yml']

        then:
        configuration
        configuration.contains("""
micronaut:
  session:
    http:
      coherence:
        enabled: true
""")
    }

    @Unroll
    void 'test gradle coherence-data feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([CoherenceData.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-data")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven coherence-data feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([CoherenceData.NAME])
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
      <artifactId>micronaut-coherence-data</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }
}
