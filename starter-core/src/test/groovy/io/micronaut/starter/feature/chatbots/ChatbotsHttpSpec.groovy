package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.chatbots.basecamp.BasecampChatbotsHttp
import io.micronaut.starter.feature.chatbots.telegram.TelegramChatbotsHttp
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Shared

class ChatbotsHttpSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    ChatbotsHttp basecampChatbotsHttp = beanContext.getBean(BasecampChatbotsHttp)

    @Shared
    ChatbotsHttp telegramChatbotsHttp = beanContext.getBean(TelegramChatbotsHttp)

    List<? extends ChatbotsHttp> getChatbotsHttpFeatures() {
        [basecampChatbotsHttp, telegramChatbotsHttp]
    }

    private static List<ApplicationType> supportedApplicationTypes() {
        (ApplicationType.values() - ApplicationType.DEFAULT) as List<ApplicationType>
    }

    void "#featureName supports function application type"(ChatbotsHttp feature, String featureName) {
        expect:
        feature.supports(ApplicationType.DEFAULT)

        where:
        feature << getChatbotsHttpFeatures()
        featureName = feature?.name
    }

    void "#featureName does not support #applicationType application type"(ApplicationType applicationType, ChatbotsHttp feature, String featureName) {
        expect:
        !feature.supports(applicationType)

        where:
        [applicationType, feature] << [supportedApplicationTypes(), getChatbotsHttpFeatures()].combinations()
        featureName = feature?.name
    }

    void 'test dependencies with #artifactId for #buildTool build with feature #featureName'(BuildTool buildTool, String featureName, String artifactId) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.DEFAULT)
                .features([featureName])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifyAll {
            verifier.hasDependency('io.micronaut.chatbots', artifactId)
        }

        where:
        buildTool               | featureName                | artifactId
        BuildTool.GRADLE        | 'chatbots-basecamp-http' | 'micronaut-chatbots-basecamp-http'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-http' | 'micronaut-chatbots-basecamp-http'
        BuildTool.MAVEN         | 'chatbots-basecamp-http' | 'micronaut-chatbots-basecamp-http'
        BuildTool.GRADLE        | 'chatbots-telegram-http' | 'micronaut-chatbots-telegram-http'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-http' | 'micronaut-chatbots-telegram-http'
        BuildTool.MAVEN         | 'chatbots-telegram-http' | 'micronaut-chatbots-telegram-http'
    }

    void 'test readme.md and function for #buildTool build with feature #featureName'(BuildTool buildTool,
                                                                                      String featureName,
                                                                                      String thirdPartyDocumentation) {
        given:
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [featureName])
        String readme = output['README.md']

        then:
        readme.contains(thirdPartyDocumentation)

        where:
        buildTool               | featureName               | thirdPartyDocumentation
        BuildTool.GRADLE        | 'chatbots-basecamp-http'  | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-http'  | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.MAVEN         | 'chatbots-basecamp-http'  | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE        | 'chatbots-telegram-http'  | "https://core.telegram.org/bots/api"
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-http'  | "https://core.telegram.org/bots/api"
        BuildTool.MAVEN         | 'chatbots-telegram-http'  | "https://core.telegram.org/bots/api"
    }
}
