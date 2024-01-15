package io.micronaut.starter.feature.awslambdacustomruntime

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaCustomRuntimeSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambdaCustomRuntime awsLambdaCustomRuntime = beanContext.getBean(AwsLambdaCustomRuntime)

    void 'test readme.md with feature aws-lambda-custom-runtime contains links to micronaut docs'() {
        when:
        def output = generate(['aws-lambda-custom-runtime'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes")
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/723")
    void 'test readme.md with feature aws-lambda-custom-runtime and graalvm contains extra documentation. language = #language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                ['aws-lambda-custom-runtime', 'graalvm']
        )

        def readme = output["README.md"]

        then:
        readme
        readme.contains("./gradlew buildNativeLambda -Pmicronaut.runtime=lambda")

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
    }

    @Unroll
    void "aws-lambda-custom-runtime does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !awsLambdaCustomRuntime.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() - [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
        description = applicationType.name
    }

    void "aws-lambda-custom-runtime supports #description application type"() {
        expect:
        awsLambdaCustomRuntime.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    @Unroll
    void 'test gradle aws-lambda-custom-runtime feature for language=#language and application: default'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-lambda-custom-runtime'])
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle aws-lambda-custom-runtime feature for language=#language and application: function'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-lambda-custom-runtime'])
                .applicationType(ApplicationType.FUNCTION)
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven aws-lambda-custom-runtime feature for language=#language and application: #applicationType'(Language language, ApplicationType applicationType) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['aws-lambda-custom-runtime'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-function-aws-custom-runtime", Scope.COMPILE)
        and: 'http-client dependency is in compile scope'
        verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE)

        where:
        language        | applicationType
        Language.JAVA   | ApplicationType.FUNCTION
        Language.GROOVY | ApplicationType.FUNCTION
        Language.KOTLIN | ApplicationType.FUNCTION
        Language.JAVA   | ApplicationType.DEFAULT
        Language.GROOVY | ApplicationType.DEFAULT
        Language.KOTLIN | ApplicationType.DEFAULT
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/721")
    void 'test maven aws-lambda-custom-runtime include http-client in compile scope: language=#language, application: #applicationType'(Language language, ApplicationType applicationType) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['aws-lambda-custom-runtime'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE)

        where:
        language        | applicationType
        Language.JAVA   | ApplicationType.FUNCTION
        Language.GROOVY | ApplicationType.FUNCTION
        Language.KOTLIN | ApplicationType.FUNCTION
        Language.JAVA   | ApplicationType.DEFAULT
        Language.GROOVY | ApplicationType.DEFAULT
        Language.KOTLIN | ApplicationType.DEFAULT
    }
}
