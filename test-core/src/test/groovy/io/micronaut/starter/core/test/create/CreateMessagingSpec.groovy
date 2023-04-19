package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.messaging.MessagingFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.IgnoreIf
import spock.lang.Unroll
import java.util.stream.Collectors

class CreateMessagingSpec extends CommandSpec {
    private static List<String> EXCLUDED_FEATURES = [
            'jms-sqs',
            'jms-aq',
    ]

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
                        .filter({ f -> !EXCLUDED_FEATURES.contains(f.name)} )
                        .map({  f -> f.getName() })
                        .collect(Collectors.toList()))
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
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
                .stream().filter { it -> it[1] == BuildTool.MAVEN }
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
        noExceptionThrown()

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations(Collections.singletonList('jms-sqs'))
                .stream()
                .filter { it -> it[1] != BuildTool.MAVEN }
    }
}
