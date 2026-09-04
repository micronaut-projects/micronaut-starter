package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.LanguageUtils

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
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language), ['kafka-streams'])
        def listener = output["src/main/${language}/example/micronaut/ExampleListener.${language.extension}"]
        def factory = output["src/main/${language}/example/micronaut/ExampleFactory.${language.extension}"]

        then:
        listener
        listener.contains("@KafkaListener(groupId = \"ExampleListener\")")
        factory
        factory.contains("builder.stream(\"streams-plaintext-input\")")

        where:
        language << LanguageUtils.JVM_LANGUAGES
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
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency("io.micronaut.kafka", "micronaut-kafka", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.kafka", "micronaut-kafka-streams", Scope.COMPILE)
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
