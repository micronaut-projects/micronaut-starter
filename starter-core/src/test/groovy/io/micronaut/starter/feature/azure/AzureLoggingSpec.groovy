package io.micronaut.starter.feature.azure

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.function.azure.AzureCloudFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class AzureLoggingSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AzureLogging feature = beanContext.getBean(AzureLogging)

    void 'azure-logging feature is in the logging category'() {
        expect:
        feature.category == Category.LOGGING
    }

    void "azure-logging supports #applicationType application type"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'azure-logging feature is an instance of AwsApiFeature'() {
        expect:
        feature instanceof AzureCloudFeature
    }

    void 'dependency added for #buildTool azure-logging feature and language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([AzureLogging.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.azure", "micronaut-azure-logging", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
