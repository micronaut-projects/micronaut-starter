package io.micronaut.starter.feature.jobrunr

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Subject

class JobRunrFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    JobRunrFeature jobRunrFeature = beanContext.getBean(JobRunrFeature)

    void "test value of third party documentation"() {
        jobRunrFeature.getThirdPartyDocumentation() == 'https://www.jobrunr.io/en/documentation/configuration/micronaut/'
    }

    void "JobRunr supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        jobRunrFeature.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "JobRunr is community feature"() {
        expect:
        jobRunrFeature.isCommunity()
    }

    void "JobRunr is jp=b processing category"() {
        expect:
        jobRunrFeature.category == Category.SCHEDULING
    }

    void "test dependency added for jobrunr feature for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([jobRunrFeature.name])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.jobrunr", "jobrunr-micronaut-feature", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void 'verify jobrunr configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext([jobRunrFeature.name])
        then:
        commandContext.configuration.get('jobrunr.background-job-server.enabled') == false
        commandContext.configuration.get('jobrunr.dashboard.enabled') == false
    }
}
