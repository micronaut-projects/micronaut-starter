package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpFunction
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpFunction
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Shared

class ChatbotsGcpSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    BasecampGcpFunction basecampGcpFunction = beanContext.getBean(BasecampGcpFunction)

    @Shared
    TelegramGcpFunction telegramGcpFunction = beanContext.getBean(TelegramGcpFunction)

    List<? extends ChatbotsGcpFunction> chatbotsGcpFunctionFeatures() {
        [basecampGcpFunction, telegramGcpFunction]
    }

    private static List<ApplicationType> unsupportedApplicationTypes() {
        (ApplicationType.values() - ApplicationType.FUNCTION) as List<ApplicationType>
    }

    void "For feature #featureName runtime is google_function"(BuildTool buildTool, ChatbotsGcpFunction feature, String featureName) {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature.name])
                .applicationType(ApplicationType.FUNCTION)
                .render()

        then:
        build.contains('runtime("google_function")')

        where:
        [buildTool, feature] << [BuildTool.valuesGradle(), chatbotsGcpFunctionFeatures()].combinations()
        featureName = feature.name
    }


    void "#featureName supports function application type"(ChatbotsGcpFunction feature, String featureName) {
        expect:
        feature.supports(ApplicationType.FUNCTION)

        where:
        feature << chatbotsGcpFunctionFeatures()
        featureName = feature?.name
    }

    void "#featureName does not support #applicationType application type"(ApplicationType applicationType, ChatbotsGcpFunction feature, String featureName) {
        expect:
        !feature.supports(applicationType)

        where:
        [applicationType, feature] << [unsupportedApplicationTypes(), chatbotsGcpFunctionFeatures()].combinations()
        featureName = feature?.name
    }

    void 'test dependencies with #artifactId for #buildTool build with feature #featureName'(BuildTool buildTool, String featureName, String artifactId) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .features([featureName])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency('io.micronaut.chatbots', artifactId)
        !verifier.hasDependency("io.micronaut.gcp:micronaut-gcp-function")

        where:
        buildTool               | featureName                      | artifactId
        BuildTool.GRADLE        | 'chatbots-basecamp-gcp-function' | 'micronaut-chatbots-basecamp-gcp-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-gcp-function' | 'micronaut-chatbots-basecamp-gcp-function'
        BuildTool.MAVEN         | 'chatbots-basecamp-gcp-function' | 'micronaut-chatbots-basecamp-gcp-function'
        BuildTool.GRADLE        | 'chatbots-telegram-gcp-function' | 'micronaut-chatbots-telegram-gcp-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-gcp-function' | 'micronaut-chatbots-telegram-gcp-function'
        BuildTool.MAVEN         | 'chatbots-telegram-gcp-function' | 'micronaut-chatbots-telegram-gcp-function'
    }

    void 'test readme.md and function for #buildTool build with feature #featureName'(BuildTool buildTool,
                                                                                      String featureName,
                                                                                      String thirdPartyDocumentation,
                                                                                      String cmd) {
        given:
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_11)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, [featureName])
        String readme = output['README.md']

        then:
        readme.contains(thirdPartyDocumentation)
        readme.contains(cmd)

        where:
        buildTool               | featureName                      | thirdPartyDocumentation                                                | cmd
        BuildTool.GRADLE        | 'chatbots-basecamp-gcp-function' | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md' | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.basecamp.googlecloud.Handler --runtime java11 --trigger-http'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-gcp-function' | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md' | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.basecamp.googlecloud.Handler --runtime java11 --trigger-http'
        BuildTool.MAVEN         | 'chatbots-basecamp-gcp-function' | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md' | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.basecamp.googlecloud.Handler --runtime java11 --trigger-http'
        BuildTool.GRADLE        | 'chatbots-telegram-gcp-function' | "https://core.telegram.org/bots/api"                                   | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.telegram.googlecloud.Handler --runtime java11 --trigger-http'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-gcp-function' | "https://core.telegram.org/bots/api"                                   | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.telegram.googlecloud.Handler --runtime java11 --trigger-http'
        BuildTool.MAVEN         | 'chatbots-telegram-gcp-function' | "https://core.telegram.org/bots/api"                                   | 'gcloud functions deploy foo --entry-point io.micronaut.chatbots.telegram.googlecloud.Handler --runtime java11 --trigger-http'
    }
}
