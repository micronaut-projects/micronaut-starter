package io.micronaut.starter.core.test.create

import io.micronaut.core.util.StringUtils
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.build.Kapt
import io.micronaut.starter.feature.messaging.MessagingFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import io.micronaut.starter.test.PredicateUtils
import spock.lang.Unroll
import java.util.stream.Collectors

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
        generateProject(lang, buildTool, [feature, Kapt.NAME], applicationType)

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [lang, buildTool, feature] << LanguageBuildCombinations.combinations(
                beanContext.streamOfType(MessagingFeature.class)
                        .map(Feature::getName)
                        .filter( f -> PredicateUtils.skipFeatureIfMacOS(List.of( 'jms-oracle-aq')).test(f))
                        .map(Feature::getName)
                        .filter( f -> !isCi() || (f != "jms-sqs" && isCi()))
                        .collect(Collectors.toList()))
    }

    private static boolean isCi() {
        StringUtils.isNotEmpty(System.getenv("CI"))
    }
}
