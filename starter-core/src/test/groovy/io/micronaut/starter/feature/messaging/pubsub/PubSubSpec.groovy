package io.micronaut.starter.feature.messaging.pubsub

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

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

    @Unroll
    void 'test gradle graalvm & gcp-pubsub features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features([PubSub.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .render()

        then:
        template.count('implementation("io.micronaut.gcp:micronaut-gcp-pubsub")') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }

    @Unroll
    void 'test maven graalvm & gcp-pubsub features adds gcp native-image support for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([PubSub.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .render()

        then:
        template.count('''
    <dependency>
      <groupId>io.micronaut.gcp</groupId>
      <artifactId>micronaut-gcp-pubsub</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        where:
        language << [Language.JAVA, Language.KOTLIN]
    }
}
