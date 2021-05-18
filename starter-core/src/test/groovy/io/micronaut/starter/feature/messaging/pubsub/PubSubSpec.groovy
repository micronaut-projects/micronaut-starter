package io.micronaut.starter.feature.messaging.pubsub

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool

class PubSubSpec extends ApplicationContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([PubSub.NAME])
                .render()

        then:
        template.contains('implementation("io.micronaut.gcp:micronaut-gcp-pubsub")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([PubSub.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-pubsub</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
