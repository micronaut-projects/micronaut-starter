package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-coherence contains links to micronaut docs'() {
        when:
        def output = generate(['cache-coherence'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#cache")
        readme.contains("https://coherence.java.net/")
    }

    @Unroll
    void 'test gradle cache-coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-coherence'])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-cache")')
        template.contains('implementation("com.oracle.coherence.ce:coherence:${coherenceVersion}")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven cache-coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-coherence'])
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
      <version>\${coherence.version}</version>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.coherence</groupId>
      <artifactId>micronaut-coherence-cache</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
