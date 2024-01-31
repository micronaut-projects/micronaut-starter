package io.micronaut.starter.feature.messaging.kafka

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class KafkaSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature kafka contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['kafka'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html")
    }

    void "testcontainers kafka dependency is present for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['kafka','testcontainers'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.testcontainers","kafka", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kafka'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
        template.contains("""
    testResources {
        sharedServer = true
    }""")
    }

    void "test resources config is not present if testContainers feature is added"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kafka', 'testcontainers'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kafka:micronaut-kafka")')
        !template.contains("""
    testResources {
        sharedServer = true
    }""")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["kafka"])
                .render()
        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kafka</groupId>
      <artifactId>micronaut-kafka</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''<artifactId>micronaut-maven-plugin</artifactId>
          <configuration>
            <shared>true</shared>
''')
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['kafka'])

        then:
        !ctx.configuration.containsKey('kafka.bootstrap.servers')
    }
}
