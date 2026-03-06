package io.micronaut.starter.feature.messaging.rabbitmq

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class RabbitMQSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature rabbitmq contains links to micronaut docs'() {
        when:
        Map<String, String>  output = generate(['rabbitmq'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/index.html")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["rabbitmq"])
                .render()

        then:
        template.contains('implementation("io.micronaut.rabbitmq:micronaut-rabbitmq")')
        template.contains('sharedServer = true')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["rabbitmq"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency("io.micronaut.rabbitmq", "micronaut-rabbitmq", Scope.COMPILE)
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
        GeneratorContext ctx = buildGeneratorContext(['rabbitmq'])

        then:
        !ctx.configuration.containsKey('rabbitmq.uri')
    }
}
