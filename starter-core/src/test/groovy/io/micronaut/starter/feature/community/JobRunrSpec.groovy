package io.micronaut.starter.feature.community

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Subject

class JobRunrSpec  extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    JobRunr jobrunr = beanContext.getBean(JobRunr)

    void "rosoco-jobrunr is a community feature"() {
        expect:
        jobrunr.isCommunity()
    }

    void "rosoco-jobrunr does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !jobrunr.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.DEFAULT)
    }

    void "rosoco-jobrunr supports function application type"() {
        expect:
        jobrunr.supports(ApplicationType.DEFAULT)
    }

    void "test dependency added for rosoco-jobrunr feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["rosoco-jobrunr"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.jobrunr", "jobrunr-micronaut-feature", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
