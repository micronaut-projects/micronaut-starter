package io.micronaut.starter.feature.retry

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class RetrySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Retry retry = beanContext.getBean(Retry)

    void 'test readme.md with feature retry contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['retry'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#retry")
    }

    @Unroll
    void "retry supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        retry.supports(MicronautOptions.builder().applicationType(applicationType).build())

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
