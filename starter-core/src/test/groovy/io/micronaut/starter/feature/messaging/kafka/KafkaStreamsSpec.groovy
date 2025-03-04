package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options

class KafkaStreamsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature kafka-streams contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['kafka-streams'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html#kafkaStream")
    }

    void 'test java project with feature kafka-streams for #language includes example listener'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .features(['kafka-streams'])
                .build()
        when:
        def output = generate(options)
        def listener = output["src/main/${language}/example/micronaut/ExampleListener.${language.extension}"]
        def factory = output["src/main/${language}/example/micronaut/ExampleFactory.${language.extension}"]

        then:
        listener
        listener.contains("@KafkaListener(groupId = \"ExampleListener\")")
        factory
        factory.contains("builder.stream(\"streams-plaintext-input\")")

        where:
        language << Language.values().toList()
    }

    void "test kafka-streams features"() {
        when:
        Features features = getFeatures(['kafka-streams'])

        then:
        features.contains("kafka")
        features.contains("kafka-streams")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kafka-streams'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka-streams")')
        template.contains('sharedServer = true')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["kafka-streams"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka-streams</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''\
    <plugins>
      <plugin>
        <groupId>io.micronaut.maven</groupId>
        <artifactId>micronaut-maven-plugin</artifactId>
        <configuration>
          <shared>true</shared>
''')
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['kafka-streams'])

        then:
        !ctx.configuration.containsKey('kafka.bootstrap.servers')
    }
}
