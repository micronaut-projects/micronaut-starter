package io.micronaut.starter.feature.retry

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

class RetrySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Retry retry = beanContext.getBean(Retry)

    @PendingFeature(reason = 'The Retry feature is for Micronaut 4, and should be visible for Starter 4.0.0')
    void "Retry feature is visible"() {
        expect:
        beanContext.getBean(Retry).isVisible()
    }

    void 'test readme.md with feature retry contains links to micronaut docs'() {
        when:
        def output = generate(['retry'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#retry")
    }

    @Unroll
    void "retry supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        retry.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    @Unroll
    void "test dependency added for retry feature for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Retry.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-retry", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
