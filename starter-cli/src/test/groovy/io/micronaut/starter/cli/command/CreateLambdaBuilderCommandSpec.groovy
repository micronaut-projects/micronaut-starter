package io.micronaut.starter.cli.command

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.architecture.CpuArchitecture
import io.micronaut.starter.feature.architecture.X86
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import org.jline.reader.LineReader
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class CreateLambdaBuilderCommandSpec extends Specification {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext

    @Shared
    CreateLambdaBuilderCommand command

    def setupSpec() {
        applicationContext = ApplicationContext.run();
        command = applicationContext.getBean(CreateLambdaBuilderCommand)
    }

    void "test lambda triggers"() {
        given:
        ApplicationContext applicationContext = ApplicationContext.run();
        CreateLambdaBuilderCommand command = applicationContext.getBean(CreateLambdaBuilderCommand)
        List<Feature> features = applicationContext.getBeansOfType(Feature)

        when:
        List<String> result = command.triggerFeatures(features).name

        then:
        result == [
                'amazon-api-gateway',
                'amazon-api-gateway-http',
                'aws-lambda-function-url',
                'aws-lambda-scheduled-event',
                'aws-lambda-s3-event-notification',
        ]

        and:
        command.apiTriggerFeatures(command.applicationTypeForCodingStyle(CodingStyle.CONTROLLERS), features).name == ['amazon-api-gateway', 'amazon-api-gateway-http']

        cleanup:
        applicationContext.close()
    }

    /*
     How do you want to write your application? (enter for Controllers
        *1) With @Controller classes, routes with methods annotated wtih @Get ...
        2) Functional style with Handler class with input and output

        Choose your trigger. (enter for Amazon API Gateway REST API)
        *1) Amazon API Gateway REST API
        2) Amazon API Gateway HTTP
        3) AWS Lambda Function URLs

        How do you want to deploy?. (enter for Java runtime)
        *1) FAT JAR running in the managed Java Runtime.
        2) Native executable built with GraalVM Native Image running in Custom Runtime. Faster Cold Start

        Choose your Lambda Architecture. (enter for Java ARM)
        *1) ARM Better performance and 20% cheaper
        2) X86

        Do you want to generate infrastructure as code with CDK? (enter for yes)
        *1) YES
        2) NO

        Choose your preferred language. (enter for default)
        *1) Java
        2) Groovy
        3) Kotlin

        Choose your preferred test framework. (enter for default)
        *1) JUnit
        2) Spock
        3) Kotest

        Choose your preferred build tool. (enter for default)
        *1) Gradle (Groovy)
        2) Gradle (Kotlin)
        3) Maven

        Choose the target JDK. (enter for default)
        *1) 11
        2) 8
     */
    void "#cliOptions.cliCommands -- #cliOptions"(LambdaCliOptions cliOptions) {
        when:
        CreateLambdaBuilderCommand command = applicationContext.getBean(CreateLambdaBuilderCommand)

        def reader = Stub(LineReader) {
            readLine(BuilderCommand.PROMPT.get()) >>> cliOptions.cliCommands
        }
        GenerateOptions options = command.createGenerateOptions(reader)

        then:
        cliOptions.expectedApplicationType == options.applicationType
        cliOptions.features ==~ options.features
        cliOptions.language == options.options.language
        cliOptions.buildTool == options.options.buildTool
        cliOptions.testFramework == options.options.testFramework
        cliOptions.javaVersion == options.options.javaVersion.asString()

        where:
        cliOptions << [CodingStyle.values(), LambdaDeployment.values()].combinations().collectMany { CodingStyle codingStyle, LambdaDeployment deployment ->
            combinations(
                    command,
                    applicationContext,
                    codingStyle == CodingStyle.CONTROLLERS ?
                            CreateLambdaBuilderCommand.apiTriggerFeatures(ApplicationType.DEFAULT, applicationContext.getBeansOfType(Feature)) :
                            CreateLambdaBuilderCommand.triggerFeatures(applicationContext.getBeansOfType(Feature)),
                    codingStyle,
                    deployment
            )
        }
    }

    static combinations(CreateLambdaBuilderCommand command, ApplicationContext ctx, List<Feature> triggerFeatures, CodingStyle codingStyle, LambdaDeployment deployment) {
        [
                triggerFeatures,
                [triggerFeatures],
                [deployment],
                [ctx.getBean(Arm), ctx.getBean(X86)],
                [true, false], // cdk
                CreateLambdaBuilderCommand.languagesForDeployment(deployment),
                [CreateLambdaBuilderCommand.languagesForDeployment(deployment)],
                [TestFramework.JUNIT, TestFramework.SPOCK, TestFramework.KOTEST],
                [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN],
                command.jdkVersionCandidates,
                [command.jdkVersionCandidates]
        ].combinations().collect { new LambdaCliOptions(codingStyle, *it) }
    }

    private static class LambdaCliOptions {

        final CodingStyle codingStyle
        final Feature apiFeature
        final LambdaDeployment lambdaDeployment
        final CpuArchitecture cpuArchitecture
        final boolean cdk
        final Language language
        final TestFramework testFramework
        final BuildTool buildTool
        final String javaVersion

        final List<Feature> allApiFeatures
        final List<Language> allLanguages
        final List<String> allJdkVersions

        LambdaCliOptions(
                CodingStyle codingStyle,
                Feature apiFeatures,
                List<Feature> allApiFeatures,
                LambdaDeployment lambdaDeployment,
                CpuArchitecture cpuArchitecture,
                boolean cdk,
                Language language,
                Language[] allLanguages,
                TestFramework testFramework,
                BuildTool buildTool,
                String javaVersion,
                List<String> allJdkVersions
        ) {
            this.codingStyle = codingStyle
            this.apiFeature = apiFeatures
            this.allApiFeatures = allApiFeatures
            this.lambdaDeployment = lambdaDeployment
            this.cpuArchitecture = cpuArchitecture
            this.cdk = cdk
            this.language = language
            this.allLanguages = allLanguages.toList()
            this.testFramework = testFramework
            this.buildTool = buildTool
            this.javaVersion = javaVersion
            this.allJdkVersions = allJdkVersions
        }

        List<String> getFeatures() {
            List<String> features = [
                    AwsLambda.FEATURE_NAME_AWS_LAMBDA,
                    apiFeature.getName(),
            ]
            if (lambdaDeployment == LambdaDeployment.NATIVE_EXECUTABLE) {
                features += [GraalVM.FEATURE_NAME_GRAALVM]
            }
            features += [cpuArchitecture.getName()]
            if (cdk) {
                features += [Cdk.NAME]
            }
            return features
        }

        ApplicationType getExpectedApplicationType() {
            if (codingStyle != CodingStyle.HANDLER) {
                return ApplicationType.DEFAULT
            }
            return ApplicationType.FUNCTION
        }

        List<String> getCliCommands() {
            [
                    "${codingStyle.ordinal() + 1}".toString(),
                    "${allApiFeatures.indexOf(apiFeature) + 1}".toString(),
                    "${lambdaDeployment.ordinal() + 1}".toString(),
                    cpuArchitecture instanceof Arm ? "1" : "2",
                    cdk ? "1" : "2",
                    "${allLanguages.indexOf(language) + 1}".toString(),
                    "${testFramework.ordinal() + 1}".toString(),
                    "${buildTool.ordinal() + 1}".toString(),
                    "${allJdkVersions.indexOf(javaVersion) + 1}".toString()
            ]
        }

        @Override
        String toString() {
            "${codingStyle} ${apiFeature?.name} ${lambdaDeployment} ${cpuArchitecture.name} cdk:${cdk} $language $testFramework $buildTool $javaVersion"
        }
    }
}
