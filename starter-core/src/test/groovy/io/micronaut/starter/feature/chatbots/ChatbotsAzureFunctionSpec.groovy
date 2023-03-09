package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsLambda
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAzureFunction
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsLambda
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureFunction
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Shared
import spock.lang.Unroll

class ChatbotsAzureFunctionSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    BasecampAzureFunction basecampAzureFunction = beanContext.getBean(BasecampAzureFunction)

    @Shared
    TelegramAzureFunction telegramAzureFunction = beanContext.getBean(TelegramAzureFunction)

    List<? extends ChatbotsAzureFunction> getChatbotsAzureFunctionFeatures() {
        [basecampAzureFunction, telegramAzureFunction]
    }

    private static List<ApplicationType> supportedApplicationTypes() {
        (ApplicationType.values() - ApplicationType.FUNCTION) as List<ApplicationType>
    }

    void "For feature #featureName runtime is azure_function"(BuildTool buildTool, ChatbotsAzureFunction feature, String featureName) {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature.name])
                .applicationType(ApplicationType.FUNCTION)
                .render()

        then:
        build.contains('runtime("azure_function")')

        where:
        [buildTool, feature] << [BuildTool.valuesGradle(), getChatbotsAzureFunctionFeatures()].combinations()
        featureName = feature.name
    }


    void "#featureName supports function application type"(ChatbotsAzureFunction feature, String featureName) {
        expect:
        feature.supports(ApplicationType.FUNCTION)

        where:
        feature << getChatbotsAzureFunctionFeatures()
        featureName = feature?.name
    }

    void "#featureName does not support #applicationType application type"(ApplicationType applicationType, ChatbotsAzureFunction feature, String featureName) {
        expect:
        !feature.supports(applicationType)

        where:
        [applicationType, feature] << [supportedApplicationTypes(), getChatbotsAzureFunctionFeatures()].combinations()
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
        verifyAll {
            verifier.hasDependency('io.micronaut.chatbots', artifactId)
        }

        where:
        buildTool               | featureName                        | artifactId
        BuildTool.GRADLE        | 'chatbots-basecamp-azure-function' | 'micronaut-chatbots-basecamp-azure-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function' | 'micronaut-chatbots-basecamp-azure-function'
        BuildTool.MAVEN         | 'chatbots-basecamp-azure-function' | 'micronaut-chatbots-basecamp-azure-function'
        BuildTool.GRADLE        | 'chatbots-telegram-azure-function' | 'micronaut-chatbots-telegram-azure-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function' | 'micronaut-chatbots-telegram-azure-function'
        BuildTool.MAVEN         | 'chatbots-telegram-azure-function' | 'micronaut-chatbots-telegram-azure-function'
    }

    void 'test dependencies with #artifactId for #buildTool build with feature #featureName'(BuildTool buildTool, String featureName, String artifactId) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .applicationType(ApplicationType.FUNCTION)
                .features([featureName])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifyAll {
            verifier.hasBuildDependency("com.microsoft.azure.azurefunctions")
            verifier.hasDependency('io.micronaut.chatbots', artifactId)
        }

        where:
        buildTool               | featureName                        | artifactId
        BuildTool.GRADLE        | 'chatbots-basecamp-azure-function' | 'micronaut-chatbots-basecamp-azure-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function' | 'micronaut-chatbots-basecamp-azure-function'
        BuildTool.GRADLE        | 'chatbots-telegram-azure-function' | 'micronaut-chatbots-telegram-azure-function'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function' | 'micronaut-chatbots-telegram-azure-function'
    }

    @Unroll("test README.md and function for #buildTool build with feature #featureName")
    void "readme verification"(BuildTool buildTool,
                               String featureName,
                               String handler,
                               String thirdPartyDocumentation) {
        given:
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, [featureName])
        String readme = output['README.md']

        then:
        verifyAll {
            readme.contains(thirdPartyDocumentation)
        }

        where:
        buildTool               | featureName                | handler                                                        | thirdPartyDocumentation
        BuildTool.GRADLE        | 'chatbots-basecamp-azure-function' | "io.micronaut.chatbots.basecamp.azurefunction.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-azure-function' | "io.micronaut.chatbots.basecamp.azurefunction.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.MAVEN         | 'chatbots-basecamp-azure-function' | "io.micronaut.chatbots.basecamp.azurefunction.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE        | 'chatbots-telegram-azure-function' | "io.micronaut.chatbots.telegram.azurefunction.Handler" | "https://core.telegram.org/bots/api"
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-azure-function' | "io.micronaut.chatbots.telegram.azurefunction.Handler" | "https://core.telegram.org/bots/api"
        BuildTool.MAVEN         | 'chatbots-telegram-azure-function' | "io.micronaut.chatbots.telegram.azurefunction.Handler" | "https://core.telegram.org/bots/api"
    }

    @Unroll
    void 'handler and test files are not generated with gradle and feature #featureName for language=#language'(Language language, String featureName) {
        given:
        String extension = language.extension

        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION,
                new Options(language, BuildTool.GRADLE,),
                [featureName]
        )

        then:
        !output.containsKey("${language.srcDir}/example/micronaut/FunctionRequestHandler.$extension".toString())
        !output.containsKey(language.getDefaults().getTest().getSourcePath("/example/micronaut/FunctionRequestHandler", language))

        where:
        [language, featureName] << [Language.values(), getChatbotsAzureFunctionFeatures()*.name].combinations()
    }
}
