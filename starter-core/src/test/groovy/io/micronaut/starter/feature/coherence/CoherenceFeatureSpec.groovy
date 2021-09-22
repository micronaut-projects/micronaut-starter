package io.micronaut.starter.feature.coherence

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceFeatureSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature coherence contains links to micronaut docs'() {
        when:
        def output = generate(['coherence'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/")
        readme.contains("https://coherence.java.net/")
    }

    void 'test gradle.properties contains coherence version'() {
        when:
        def output = generate(['coherence'])
        def properties = output["gradle.properties"]

        then:
        properties
        properties.contains("coherenceVersion=${CoherenceFeature.COHERENCE_VERSION}")
    }

    @Unroll
    void 'test gradle coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['coherence'])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("com.oracle.coherence.ce:coherence:${coherenceVersion}")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven coherence-data feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['coherence-data'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.coherence</groupId>
      <artifactId>micronaut-coherence</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''
    <dependency>
      <groupId>com.oracle.coherence.ce</groupId>
      <artifactId>coherence</artifactId>
      <version>${coherence.version}</version>
      <scope>compile</scope>
    </dependency>
''')
        where:
        language << Language.values().toList()
    }
}
