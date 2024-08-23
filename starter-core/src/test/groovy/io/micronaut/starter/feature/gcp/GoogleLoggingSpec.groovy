package io.micronaut.starter.feature.gcp

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.gcp.GcpCloudFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject

class GoogleLoggingSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    GoogleLogging feature = beanContext.getBean(GoogleLogging)

    void 'gcp-logging feature is in the logging category'() {
        expect:
        feature.category == Category.LOGGING
    }

    void "gcp-logging supports #applicationType application type"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'gcp-logging feature is an instance of AwsApiFeature'() {
        expect:
        feature instanceof GcpCloudFeature
    }

    void 'dependency added for #buildTool gcp-logging feature and language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([GoogleLogging.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.gcp", "micronaut-gcp-logging", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
