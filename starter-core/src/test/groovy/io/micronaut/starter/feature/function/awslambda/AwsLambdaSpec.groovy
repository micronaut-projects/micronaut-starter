package io.micronaut.starter.feature.function.awslambda

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambda awsLambda = beanContext.getBean(AwsLambda)

    @Unroll
    void "aws-lambda does not support #description"(ApplicationType applicationType,
                                                    String description) {
        expect:
        !awsLambda.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.DEFAULT,
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
        description = applicationType.name
    }

    void "aws-lambda supports function application type"() {
        expect:
        awsLambda.supports(ApplicationType.FUNCTION)
    }

    @Unroll
    void 'aws-lambda is the default feature for function for gradle and language=#language'(Language language) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures([], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-function-aws")')

        where:
        language << Language.values()
    }


    @Unroll
    void 'test gradle aws-lambda feature for language=#language'(Language language) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION)).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-function-aws")')

        where:
        language << Language.values()
    }


    @Unroll
    void 'aws-lambda feature is default feature for function and language=#language'(Language language) {
        when:
        String template = pom.template(buildProject(), getFeatures([], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-function-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven aws-lambda feature for language=#language'(Language language) {
        when:
        String template = pom.template(buildProject(), getFeatures(['aws-lambda'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-function-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'function with gradle and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language),
                ['aws-lambda']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

        then:
        build.contains('implementation("io.micronaut:micronaut-function-aws")')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookMicronautRequestHandler.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookMicronautRequestHandlerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'function with maven and feature aws-lambda for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda']
        )
        String build = output['pom.xml']
        def readme = output["README.md"]

        then:
        build.contains('<artifactId>micronaut-function-aws</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        !build.contains('<artifactId>micronaut-http-client</artifactId>')

        output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookSaved.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/BookMicronautRequestHandler.$extension".toString())
        output.containsKey("$testSrcDir/example/micronaut/BookMicronautRequestHandlerTest.$extension".toString())

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }
}
