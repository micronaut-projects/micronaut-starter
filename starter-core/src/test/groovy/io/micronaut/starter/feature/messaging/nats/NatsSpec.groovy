package io.micronaut.starter.feature.messaging.nats

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool

class NatsSpec extends ApplicationContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["nats"])
                .render()

        then:
        template.contains('implementation("io.micronaut.nats:micronaut-nats")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["nats"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency("io.micronaut.nats", "micronaut-nats", Scope.COMPILE)
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['nats'])

        then:
        ctx.configuration.containsKey('nats.default.addresses')
    }
}
