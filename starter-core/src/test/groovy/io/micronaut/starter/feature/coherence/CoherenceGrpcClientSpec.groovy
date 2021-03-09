package io.micronaut.starter.feature.coherence

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CoherenceGrpcClientSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature coherence-grpc-client contains links to micronaut docs'() {
        when:
        def output = generate(['coherence-grpc-client'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-coherence/1.0.x/guide/index.html#grpc")
        readme.contains("https://coherence.java.net/")
    }

    void 'test configuration with feature coherence-grpc-client'() {
        when:
        def output = generate(['coherence-grpc-client'])
        def configuration = output['src/main/resources/application.yml']

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
                .features(['coherence-grpc-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence")')
        template.contains('implementation("io.micronaut.coherence:micronaut-coherence-grpc-client")')
        template.contains('implementation("com.oracle.coherence.ce:coherence:${coherenceVersion}")')
        template.contains('implementation("com.oracle.coherence.ce:coherence-java-client:${coherenceVersion}")')

        where:
        language << Language.values().toList()
    }
    @Unroll
    void 'test maven coherence-grpc-client feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['coherence-grpc-client'])
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
      <artifactId>micronaut-coherence-grpc-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.oracle.coherence.ce</groupId>
      <artifactId>coherence-java-client</artifactId>
      <version>\${coherence.version}</version>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
