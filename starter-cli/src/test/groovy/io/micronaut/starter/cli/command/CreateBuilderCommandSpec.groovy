package io.micronaut.starter.cli.command

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.aws.LambdaTrigger
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import org.jline.reader.LineReader
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

class CreateBuilderCommandSpec extends Specification {
    /*
    What type of application do you want to create? (enter for default)
*1) Micronaut Application
 2) Micronaut CLI Application
 3) Micronaut Serverless Function
 4) Micronaut gRPC Application
 5) Micronaut Messaging Application

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
 1) 8
*2) 11
 3) 17
     */
    @Unroll
    void "test prompt"(List<String> answers,
                       ApplicationType applicationType,
                       Language language,
                       TestFramework testFramework,
                       BuildTool buildTool,
                       JdkVersion jdkVersion) {
        given:
        ApplicationContext applicationContext = ApplicationContext.run();
        CreateBuilderCommand command = applicationContext.getBean(CreateBuilderCommand)

        when:
        def reader = Stub(LineReader) {
            readLine(BuilderCommand.PROMPT) >>> answers

        }
        GenerateOptions options = command.createGenerateOptions(reader)

        then:
        applicationType == options.applicationType
        language == options.options.language
        testFramework == options.options.testFramework
        buildTool == options.options.buildTool
        jdkVersion == options.options.javaVersion

        cleanup:
        applicationContext.close()

        where:
        answers | applicationType | language | testFramework | buildTool | jdkVersion
        [
                "1", // Default
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "2", // CLI
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.CLI | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "3", // FUNCTION
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.FUNCTION | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "4", // GRPC
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.GRPC | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "5", // MESSAGING
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.MESSAGING | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "1", // Default
                "2", // Groovy
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.GROOVY | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "1", // Default
                "3", // Kotlin
                "1", // JUnit
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.KOTLIN | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "1", // Default
                "1", // Java
                "2", // Spock
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.SPOCK | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "1", // Default
                "1", // Java
                "3", // KoTest
                "1", // Gradle
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.KOTEST | BuildTool.GRADLE | JdkVersion.JDK_8
        [
                "1", // Default
                "1", // Java
                "1", // JUnit
                "2", // Gradle Kotlin
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE_KOTLIN | JdkVersion.JDK_8
        [
                "1", // Default
                "1", // Java
                "1", // JUnit
                "3", // Maven
                "1", // JDK 8
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.JUNIT | BuildTool.MAVEN | JdkVersion.JDK_8
        [
                "1", // Default
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "2", // JDK 11
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_11
        [
                "1", // Default
                "1", // Java
                "1", // JUnit
                "1", // Gradle
                "3", // JDK 17
        ]       | ApplicationType.DEFAULT | Language.JAVA | TestFramework.JUNIT | BuildTool.GRADLE | JdkVersion.JDK_17
    }
}
