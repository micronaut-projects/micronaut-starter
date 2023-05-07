package io.micronaut.starter.core.test.create

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.messaging.MessagingFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

import java.util.stream.Collectors

class CreateMessagingSpec extends CommandSpec {
    private static List<String> EXCLUDED_FEATURES = [
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
}
