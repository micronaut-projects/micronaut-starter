package io.micronaut.starter.feature.websocket

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class WebsocketSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Websocket websocket = beanContext.getBean(Websocket)

    @PendingFeature(reason = 'The Websocket feature is for Micronaut 4, and should be visible for Starter 4.0.0')
    void "Websocket feature is visible"() {
        expect:
        beanContext.getBean(Websocket).isVisible()
    }

    void 'test readme.md with feature websocket contains links to micronaut docs'() {
        when:
        def output = generate([Websocket.NAME])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#websocket")
    }

    @Unroll
    void "test dependency added for websocket feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Websocket.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-websocket", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    @Unroll
    void "websocket supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        websocket.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }
}
