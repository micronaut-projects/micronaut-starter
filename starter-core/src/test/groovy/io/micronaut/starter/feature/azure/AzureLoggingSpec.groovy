package io.micronaut.starter.feature.azure

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.function.azure.AzureCloudFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
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
