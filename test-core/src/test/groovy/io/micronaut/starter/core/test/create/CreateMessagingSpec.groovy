package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.messaging.MessagingFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import org.gradle.testkit.runner.UnexpectedBuildFailure
import spock.lang.Unroll

import java.util.stream.Collectors

import static io.micronaut.starter.options.BuildTool.MAVEN

class CreateMessagingSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        "test-messaging-createmessagingspec"
    }

    @Unroll
    void 'test basic create-messaging-app for #feature and #lang and #buildTool'(Language lang,
                                                                                 BuildTool buildTool,
                                                                                 String feature) {
        given:
        ApplicationType applicationType = ApplicationType.MESSAGING
        generateProject(lang, buildTool, [feature], applicationType)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations(
                beanContext.streamOfType(MessagingFeature.class)
                        .filter({ f -> f.name != 'jms-sqs'} )
                        .map({  f -> f.getName() })
                        .collect(Collectors.toList()))
    }

    @Unroll
    void 'test basic create-messaging-app for jms-sqs and #lang and maven'(Language lang,
                                                                           BuildTool buildTool,
                                                                           String feature) {
        given:
        ApplicationType applicationType = ApplicationType.MESSAGING
        generateProject(lang, buildTool, [feature], applicationType)

        when:
        String output = executeBuild(buildTool, 'test')

        then:
        output.contains 'No bean of type [com.amazonaws.services.sqs.AmazonSQS] exists'

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations(
                beanContext.streamOfType(MessagingFeature.class)
                        .filter({ f -> f.name == 'jms-sqs'} )
                        .map({  f -> f.getName() })
                        .collect(Collectors.toList()))
                .stream().filter { it -> it[1] == MAVEN }
    }

    @Unroll
    void 'test basic create-messaging-app for jms-sqs and #lang and #buildTool'(Language lang,
                                                                                BuildTool buildTool,
                                                                                String feature) {
        given:
        ApplicationType applicationType = ApplicationType.MESSAGING
        generateProject(lang, buildTool, [feature], applicationType)

        when:
        executeBuild buildTool, 'test'

        then:
        thrown UnexpectedBuildFailure

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations(
                beanContext.streamOfType(MessagingFeature.class)
                        .filter({ f -> f.name == 'jms-sqs'} )
                        .map({  f -> f.getName() })
                        .collect(Collectors.toList()))
                .stream().filter { it -> it[1] != MAVEN }
    }
}
