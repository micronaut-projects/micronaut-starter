package io.micronaut.starter.feature.aws

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.template.Template
import spock.lang.Subject

class CdkFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    Cdk cdk = beanContext.getBean(Cdk)

    CoordinateResolver resolver = beanContext.getBean(CoordinateResolver)

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

    void 'Function AppStack log retention is included for #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [Cdk.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.logs.RetentionDays;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.logRetention(RetentionDays.ONE_WEEK)')

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN , BuildTool.MAVEN]
    }

    void "dependencies are added for cdk to infra project for #buildTool"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])

        then:
        output."$Cdk.INFRA_MODULE/$buildFile".contains($/implementation("io.micronaut.aws:micronaut-aws-cdk/$)

        where:
        buildTool               | buildFile
        BuildTool.GRADLE        | "build.gradle"
        BuildTool.GRADLE_KOTLIN | "build.gradle.kts"
    }

    void "dependencies are added for cdk to infra project for maven"() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.MAVEN), [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])
        def dependency = new XmlParser().parseText(output."$Cdk.INFRA_MODULE/pom.xml").dependencies.dependency.find {
            it.artifactId.text() == 'micronaut-aws-cdk'
        }

        then:
        with(dependency) {
            it.groupId.text() == 'io.micronaut.aws'
        }
    }
}
