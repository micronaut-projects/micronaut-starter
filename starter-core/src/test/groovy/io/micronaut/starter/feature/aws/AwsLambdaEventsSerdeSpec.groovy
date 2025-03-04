package io.micronaut.starter.feature.aws

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaEventsSerdeSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambdaEventsSerde feature = beanContext.getBean(AwsLambdaEventsSerde)

    void "aws-lambda-events-serde supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        feature.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "aws-lambda-events-serde overrides Feature->getFrameworkDocumentation"() {
        expect:
        feature.getFrameworkDocumentation(null)
    }

    void "aws-lambda-events-serde overrides Feature->getThirdPartyDocumentation"() {
        expect:
        feature.getThirdPartyDocumentation(null)
    }

    void "aws-lambda-events-serde belongs to API category"() {
        expect:
        Category.SERVERLESS == feature.category
    }

    @Unroll
    void 'test aws-lambda-events-serde feature adds dependency for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['aws-lambda-events-serde'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-lambda-events-serde", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }
}
