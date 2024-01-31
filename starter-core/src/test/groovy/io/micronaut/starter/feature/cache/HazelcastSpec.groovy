package io.micronaut.starter.feature.cache

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class HazelcastSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test readme.md contains links to hazelcast and micronaut docs'() {
        when:
        Map<String, String> output = generate(['cache-hazelcast'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://hazelcast.org/")
        readme.contains("https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html#hazelcast")
    }

    @Unroll
    void 'test gradle cache-hazelcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['cache-hazelcast'])
                .render()

        then:
        template.contains('implementation("io.micronaut.cache:micronaut-cache-hazelcast")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven cache-hazelcast feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['cache-hazelcast'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-hazelcast</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test cache-hazelcast configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['cache-hazelcast'])

        then:
        commandContext.configuration.get('hazelcast.network.addresses'.toString()) == "['121.0.0.1:5701']"
    }

}
