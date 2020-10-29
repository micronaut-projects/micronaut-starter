package io.micronaut.starter.feature.messaging.pubsub

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class PubSubSpec extends BeanContextSpec {

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([PubSub.NAME]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.gcp:micronaut-gcp-pubsub")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures([PubSub.NAME]), []).render().toString()

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
