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
        def output = generate([CoherenceFeature.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/")
        readme.contains("https://coherence.java.net/")
    }

    @Unroll
    void 'test gradle coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([CoherenceFeature.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven coherence feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([CoherenceFeature.NAME])
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
      <scope>compile</scope>
    </dependency>
''')
        where:
        language << Language.values().toList()
    }
}
