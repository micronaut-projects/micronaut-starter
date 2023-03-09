package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsLambda
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Unroll

class ChatbotsAwsLambdaSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    BasecampAwsLambda basecampAwsLambda = beanContext.getBean(BasecampAwsLambda)

    @Shared
    TelegramAwsLambda telegramAwsLambda = beanContext.getBean(TelegramAwsLambda)

    List<? extends ChatbotsAwsLambda> getChatbotsAwsLambdaFeatures() {
        [basecampAwsLambda, telegramAwsLambda]
    }

    private static List<ApplicationType> supportedApplicationTypes() {
        (ApplicationType.values() - ApplicationType.FUNCTION) as List<ApplicationType>
    }

    void "For feature #featureName runtime is lambda_java"(BuildTool buildTool, ChatbotsAwsLambda feature, String featureName) {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature.name])
                .applicationType(ApplicationType.FUNCTION)
                .render()

        then:
        build.contains('runtime("lambda_java")')

        where:
        [buildTool, feature] << [BuildTool.valuesGradle(), getChatbotsAwsLambdaFeatures()].combinations()
        featureName = feature.name
    }


    void "#featureName supports function application type"(ChatbotsAwsLambda feature, String featureName) {
        expect:
        feature.supports(ApplicationType.FUNCTION)

        where:
        feature << getChatbotsAwsLambdaFeatures()
        featureName = feature?.name
    }

    void "#featureName does not support #applicationType application type"(ApplicationType applicationType, ChatbotsAwsLambda feature, String featureName) {
        expect:
        !feature.supports(applicationType)

        where:
        [applicationType, feature] << [supportedApplicationTypes(), getChatbotsAwsLambdaFeatures()].combinations()
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
            !verifier.hasDependency('com.amazonaws', 'aws-lambda-java-events')
            !verifier.hasDependency("io.micronaut.aws:micronaut-function-aws")
        }

        where:
        buildTool               | featureName                | artifactId
        BuildTool.GRADLE        | 'chatbots-basecamp-lambda' | 'micronaut-chatbots-basecamp-lambda'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-lambda' | 'micronaut-chatbots-basecamp-lambda'
        BuildTool.MAVEN         | 'chatbots-basecamp-lambda' | 'micronaut-chatbots-basecamp-lambda'
        BuildTool.GRADLE        | 'chatbots-telegram-lambda' | 'micronaut-chatbots-telegram-lambda'
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-lambda' | 'micronaut-chatbots-telegram-lambda'
        BuildTool.MAVEN         | 'chatbots-telegram-lambda' | 'micronaut-chatbots-telegram-lambda'
    }

    @Unroll("test README.md and function for #buildTool build with feature #featureName")
    void "readme verification"(BuildTool buildTool,
                               String featureName,
                               String handler,
                               String micronautDocs) {
        given:
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, [featureName])
        String readme = output['README.md']

        then:
        verifyAll {
            readme.contains(micronautDocs)
            readme.contains("[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)")
            readme.contains("Handler: $handler".toString())
        }

        where:
        buildTool               | featureName                | handler                                         | micronautDocs
        BuildTool.GRADLE        | 'chatbots-basecamp-lambda' | "io.micronaut.chatbots.basecamp.lambda.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE_KOTLIN | 'chatbots-basecamp-lambda' | "io.micronaut.chatbots.basecamp.lambda.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.MAVEN         | 'chatbots-basecamp-lambda' | "io.micronaut.chatbots.basecamp.lambda.Handler" | 'https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md'
        BuildTool.GRADLE        | 'chatbots-telegram-lambda' | "io.micronaut.chatbots.telegram.lambda.Handler" | "https://core.telegram.org/bots/api"
        BuildTool.GRADLE_KOTLIN | 'chatbots-telegram-lambda' | "io.micronaut.chatbots.telegram.lambda.Handler" | "https://core.telegram.org/bots/api"
        BuildTool.MAVEN         | 'chatbots-telegram-lambda' | "io.micronaut.chatbots.telegram.lambda.Handler" | "https://core.telegram.org/bots/api"
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
        [language, featureName] << [Language.values(), ['chatbots-basecamp-lambda', 'chatbots-telegram-lambda']].combinations()
    }
}
