package io.micronaut.starter.feature.awsapiproxy

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class AwsApiGatewayLambdaProxySpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsApiGatewayLambdaProxy feature = beanContext.getBean(AwsApiGatewayLambdaProxy)

    @Unroll
    void 'Application file is NOT generated for a default application type with gradle and features aws-api-gateway-lambda-proxy for language: #description'(Language language, String extension, String description) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE),
                ['aws-api-gateway-lambda-proxy']
        )

        then:
        !output.containsKey("src/main/java/example/micronaut/Application.${extension}".toString())

        when:
        def buildGradle = output['build.gradle']

        then:
        buildGradle.contains('mainClassName = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime"')

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        description = language.name
    }


    @Unroll
    void "aws-api-gateway-lambda-proxy does not support #description"(ApplicationType applicationType,
                                                    String description) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.DEFAULT)
        description = applicationType.name
    }

    void "aws-lambda supports function application type"() {
        expect:
        feature.supports(ApplicationType.DEFAULT)
    }

    @Unroll
    void 'aws-api-gateway-lambda-proxy is the default feature for function for gradle and language=#language'(Language language) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['aws-api-gateway-lambda-proxy'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-api-proxy")')
        !template.contains('implementation("io.micronaut:micronaut-http-server-netty")')
        !template.contains('implementation("io.micronaut:micronaut-http-client")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven micronaut-function-aws-api-proxy feature for language=#language'(Language language) {
        when:
        String template = pom.template(buildProject(), getFeatures(['aws-api-gateway-lambda-proxy'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-api-proxy</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }

    @Unroll
    void 'app with gradle and feature aws-api-gateway-lambda-proxy and graalvm applies aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language),
                ['aws-api-gateway-lambda-proxy', 'graalvm']
        )
        String build = output['build.gradle']

        then:
        build.contains('implementation("io.micronaut.aws:micronaut-function-aws-api-proxy")')
        build.contains('implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    @Unroll
    void 'app with maven and feature aws-api-gateway-lambda-proxy and graalvm applies aws-lambda-custom-runtime for language=#language'() {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN),
                ['aws-api-gateway-lambda-proxy', 'graalvm']
        )
        String build = output['pom.xml']

        then:
        build.contains('<artifactId>micronaut-function-aws-api-proxy</artifactId>')
        build.contains('<artifactId>micronaut-function-aws-custom-runtime</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        !build.contains('<artifactId>micronaut-http-client</artifactId>')

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

}
