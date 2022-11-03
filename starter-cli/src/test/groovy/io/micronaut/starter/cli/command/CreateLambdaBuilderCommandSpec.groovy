package io.micronaut.starter.cli.command

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.aws.LambdaTrigger
import spock.lang.Specification
import org.jline.reader.LineReader
import spock.lang.Unroll

import java.util.stream.Collectors

class CreateLambdaBuilderCommandSpec extends Specification {

    void "test lambda triggers"() {
        given:
        ApplicationContext applicationContext = ApplicationContext.run();
        CreateLambdaBuilderCommand command = applicationContext.getBean(CreateLambdaBuilderCommand)
        Collection<Feature> features = applicationContext.getBeansOfType(LambdaTrigger)

        when:
        List<String> result = command.triggerFeatures(features)
                .stream()
                .map(f -> f.getName())
                .collect(Collectors.toList());
        then:
        [
                'amazon-api-gateway',
                'amazon-api-gateway-http',
                'aws-lambda-function-url',
                'aws-lambda-scheduled-event',
                'aws-lambda-s3-event-notification',
        ] == result

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
    @Unroll
    void "test prompt"(List<String> answers, ApplicationType expectedApplicationType, Set<String> expectedFeatures) {
        given:
        ApplicationContext applicationContext = ApplicationContext.run();
        CreateLambdaBuilderCommand command = applicationContext.getBean(CreateLambdaBuilderCommand)

        when:
        def reader = Stub(LineReader) {
            readLine(BuilderCommand.PROMPT.get()) >>> answers

        }
        GenerateOptions options = command.createGenerateOptions(reader)

        then:
        expectedApplicationType == options.applicationType
        expectedFeatures == options.features

        cleanup:
        applicationContext.close()

        where:
        answers | expectedApplicationType | expectedFeatures
        [
                "1", // with Controller
                "1", // Amazon Api Gateway
                "1",  // FAT JAR
                "1",  // ARM
                "1",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.DEFAULT | ['amazon-api-gateway', 'arm', 'aws-cdk','aws-lambda'] as Set<String>
        [
                "2", // with Controller
                "1", // Amazon Api Gateway
                "1",  // FAT JAR
                "1",  // ARM
                "1",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.FUNCTION | ['amazon-api-gateway', 'arm', 'aws-cdk','aws-lambda'] as Set<String>
        [
                "2", // with Controller
                "4", // Scheduled event
                "1",  // FAT JAR
                "1",  // ARM
                "1",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.FUNCTION | ['aws-lambda-scheduled-event', 'arm', 'aws-cdk','aws-lambda'] as Set<String>
        [
                "1", // with Controller
                "1", // Amazon Api Gateway
                "2",  // FAT JAR
                "1",  // ARM
                "1",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.DEFAULT | ['amazon-api-gateway', 'arm', 'aws-cdk','aws-lambda','graalvm'] as Set<String>
        [
                "1", // with Controller
                "1", // Amazon Api Gateway
                "1",  // FAT JAR
                "2",  // x86
                "1",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.DEFAULT | ['amazon-api-gateway', 'x86', 'aws-cdk','aws-lambda'] as Set<String>
        [
                "1", // with Controller
                "1", // Amazon Api Gateway
                "1",  // FAT JAR
                "1",  // ARM
                "2",  // CDK Yes
                "1",  // Java
                "1",  // Junit
                "1",  // Gradle Groovy DSL
                "1",  // Java 11
        ]       | ApplicationType.DEFAULT | ['amazon-api-gateway','arm','aws-lambda'] as Set<String>



    }
}
