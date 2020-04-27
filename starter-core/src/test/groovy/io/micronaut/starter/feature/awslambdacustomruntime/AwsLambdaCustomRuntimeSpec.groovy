package io.micronaut.starter.feature.awslambdacustomruntime

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

class AwsLambdaCustomRuntimeSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambdaCustomRuntime awsLambdaCustomRuntime = beanContext.getBean(AwsLambdaCustomRuntime)

    @Unroll
    void 'verify bootstrap for a function application type with gradle and feature aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['aws-lambda-custom-runtime']
        )
        String bootstrap = output['bootstrap']

        then:
        bootstrap.contains('#!/bin/sh')
        bootstrap.contains('set -euo pipefail')
        bootstrap.contains('java -XX:TieredStopAtLevel=1 -noverify -cp foo-all.jar example.micronaut.BookLambdaRuntime')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'verify bootstrap for a function application type with maven and feature aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda-custom-runtime']
        )
        String bootstrap = output['bootstrap']

        then:
        bootstrap.contains('#!/bin/sh')
        bootstrap.contains('set -euo pipefail')
        bootstrap.contains('java -XX:TieredStopAtLevel=1 -noverify -cp foo.jar example.micronaut.BookLambdaRuntime')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'verify bootstrap for a function application type with maven and feature graalvm for language=#language aws-lambda-custom-runtime shoudl be included even if not specified '() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['graalvm']
        )
        String bootstrap = output['bootstrap']

        then:
        bootstrap.contains('#!/bin/sh')
        bootstrap.contains('set -euo pipefail')
        bootstrap.contains('./foo -Xmx128m -Djava.library.path=$(pwd)')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'verify bootstrap for a function application type with maven and feature aws-lambda-custom-runtime and graalvm for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-lambda-custom-runtime', 'graalvm']
        )
        String bootstrap = output['bootstrap']

        then:
        bootstrap.contains('#!/bin/sh')
        bootstrap.contains('set -euo pipefail')
        bootstrap.contains('./foo -Xmx128m -Djava.library.path=$(pwd)')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void "aws-lambda-custom-runtime does not support #description"(ApplicationType applicationType, String description) {
        expect:
        !awsLambdaCustomRuntime.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-lambda-custom-runtime'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle aws-lambda-custom-runtime feature for language=#language and application: function'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-lambda-custom-runtime'], language, null, BuildTool.GRADLE, ApplicationType.FUNCTION)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven aws-lambda-custom-runtime feature for language=#language and application: #applicationType'(Language language, ApplicationType applicationType) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['aws-lambda-custom-runtime'], language, null, BuildTool.MAVEN,applicationType), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-custom-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
""")

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
