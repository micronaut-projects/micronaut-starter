package io.micronaut.starter.feature.awsalexa

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AwsAlexaSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsAlexa alexaFunction = beanContext.getBean(AwsAlexa)

    void 'test readme.md with feature aws-alexa  contains links to micronaut docs'() {
        when:
        def output = generate(['aws-alexa'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#alexa")
    }

    @Unroll
    void "aws-alexa does not support #description"(ApplicationType applicationType,
                                                        String description) {
        expect:
        !alexaFunction.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList() - [
                ApplicationType.DEFAULT,
                ApplicationType.FUNCTION
        ]
        description = applicationType.name
    }

    @Unroll
    void "aws-alexa supports #description application type"() {
        expect:
        alexaFunction.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.FUNCTION]
        description = applicationType.name
    }

    @Unroll
    void 'test gradle aws-alexa feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.FUNCTION, buildProject(), getFeatures(['aws-alexa'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-alexa")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven aws-alexa feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.FUNCTION, buildProject(),
                getFeatures(['aws-alexa'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-alexa</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }



    @Unroll
    void 'default app with gradle aws-alexa feature for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-alexa'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-alexa-httpserver")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'default app with maven aws-alexa feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-alexa'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-alexa-httpserver</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList().toList()
    }

    @Unroll
    void 'app with gradle and feature aws-alexa for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, BuildTool.GRADLE),
                ['aws-alexa']
        )

        then:
        output.containsKey("$srcDir/example/micronaut/CancelIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/FallbackIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/HelpIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/LaunchRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/SessionEndedRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/StopIntentHandler.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'book pojos and request handler are not generated for function, even if aws-lambda is the default feature, if you apply feature aws-alexa with maven for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, BuildTool.MAVEN),
                ['aws-alexa']
        )

        then:
        !output.containsKey("$srcDir/example/micronaut/BookRequestHandler.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/CancelIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/FallbackIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/HelpIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/LaunchRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/SessionEndedRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/StopIntentHandler.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'book pojos and request handler are not generated for function, even if aws-lambda is the default feature, if you apply feature aws-alexa with gradle for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language),
                ['aws-alexa']
        )

        then:
        !output.containsKey("$srcDir/example/micronaut/BookRequestHandler.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/CancelIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/FallbackIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/HelpIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/LaunchRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/SessionEndedRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/StopIntentHandler.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'app with maven and feature aws-alexa for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-alexa']
        )

        then:
        output.containsKey("$srcDir/example/micronaut/CancelIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/FallbackIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/HelpIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/LaunchRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/SessionEndedRequestIntentHandler.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/StopIntentHandler.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}