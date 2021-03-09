package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature cache-coherence contains links to micronaut docs'() {
        when:
        def output = generate(['cache-coherence'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#coherence")
        readme.contains("https://coherence.java.net/")
    }

    @Unroll
    void 'test gradle cache-coherence feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cache-coherence'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-cache")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven cache-coherence feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['cache-coherence'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.coherence</groupId>
      <artifactId>micronaut-coherence-cache</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.oracle.coherence.ce</groupId>
      <artifactId>coherence</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
