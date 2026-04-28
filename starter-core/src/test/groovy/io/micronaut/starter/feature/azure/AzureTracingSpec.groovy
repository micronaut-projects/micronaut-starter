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

import static io.micronaut.starter.build.dependencies.Scope.COMPILE
import static io.micronaut.starter.feature.Category.TRACING

class AzureTracingSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AzureTracing feature = beanContext.getBean(AzureTracing)

    void 'azure-tracing feature is in the tracing category'() {
        expect:
        feature.category == TRACING
    }

    void 'azure-tracing supports #applicationType application type'(ApplicationType applicationType) {
        expect:
        feature.supports applicationType

        where:
        applicationType << ApplicationType.values()
    }

    void 'azure-tracing feature is an instance of AwsApiFeature'() {
        expect:
        feature instanceof AzureCloudFeature
    }

    void 'dependency added for #buildTool azure-tracing feature and language=#language'(Language language,
                                                                                        BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([AzureTracing.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency('io.micronaut.azure', 'micronaut-azure-tracing', COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
