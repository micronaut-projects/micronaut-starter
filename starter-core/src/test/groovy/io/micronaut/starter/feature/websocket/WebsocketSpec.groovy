package io.micronaut.starter.feature.websocket

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature
import spock.lang.Unroll

class WebsocketSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @PendingFeature(reason = 'The Websocket feature is for Micronaut 4, and should be visible for Starter 4.0.0')
    void "Websocket feature is visible"() {
        expect:
        beanContext.getBean(Websocket).isVisible()
    }

    void 'test readme.md with feature websocket contains links to micronaut docs'() {
        when:
        def output = generate(['websocket'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#websocket")
    }

    @Unroll
    void 'test gradle websocket feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['websocket'])
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-websocket")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven websocket feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['websocket'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-websocket</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
