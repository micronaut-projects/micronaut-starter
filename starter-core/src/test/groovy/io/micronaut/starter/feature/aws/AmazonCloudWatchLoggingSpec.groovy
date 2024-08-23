package io.micronaut.starter.feature.aws;

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.fixture.CommandOutputFixture;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject;

class AmazonCloudWatchLoggingSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AmazonCloudWatchLogging feature = beanContext.getBean(AmazonCloudWatchLogging)

    void 'amazon-cloudwatch-logging feature is in the logging category'() {
        expect:
        feature.category == Category.LOGGING
    }

    void "amazon-cloudwatch-logging supports #applicationType application type"(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'amazon-cloudwatch-logging feature is an instance of AwsApiFeature'() {
        expect:
        feature instanceof AwsFeature
    }

    void 'dependency added for #buildTool amazon-cloudwatch-logging feature and language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([AmazonCloudWatchLogging.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-cloudwatch-logging", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }

}