package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.template.Template
import spock.lang.Shared
import spock.lang.Subject

class CdkFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Cdk cdk = beanContext.getBean(Cdk)

    void 'cdk feature is in the cloud category'() {
        expect:
        cdk.category == Category.CLOUD
    }

    void 'submodules are created for #buildTool'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])

        then:
        output.'micronaut-cli.yml'
        output."$Template.DEFAULT_MODULE/$buildFile"
        output."$Cdk.INFRA_MODULE/cdk.json"
        output."$Cdk.INFRA_MODULE/$buildFile"

        output.'README.md'.contains('## Feature aws-cdk documentation')
        output.'README.md'.contains('[https://docs.aws.amazon.com/cdk/v2/guide/home.html](https://docs.aws.amazon.com/cdk/v2/guide/home.html)')

        where:
        buildTool               | buildFile
        BuildTool.GRADLE        | "build.gradle"
        BuildTool.GRADLE_KOTLIN | "build.gradle.kts"
        BuildTool.MAVEN         | "pom.xml"
    }

    void 'missing lambda feature is invalid'() {
        when:
        def output = generate([Cdk.NAME])

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'CDK requires AWS Lambda'
    }
}
