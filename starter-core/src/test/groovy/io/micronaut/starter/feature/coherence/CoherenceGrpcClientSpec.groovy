package io.micronaut.starter.feature.coherence

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceGrpcClientSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature coherence-grpc-client contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([CoherenceGrpcClient.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#grpc")
        readme.contains("https://coherence.java.net/")
    }

    void 'test configuration with feature coherence-grpc-client'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, CoherenceGrpcClient.NAME])
        String configuration = output['src/main/resources/application.yml']

        then:
        configuration
        configuration.contains("""
coherence.session.default.type: grpc
""")
    }

    @Unroll
    void 'test gradle coherence-grpc-client feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([CoherenceGrpcClient.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-grpc-client")')
        template.contains('implementation("com.oracle.coherence.ce:coherence")')
        template.contains('implementation("com.oracle.coherence.ce:coherence-java-client")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven coherence-grpc-client feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([CoherenceGrpcClient.NAME])
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
      <artifactId>micronaut-coherence-grpc-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.oracle.coherence.ce</groupId>
      <artifactId>coherence-java-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
