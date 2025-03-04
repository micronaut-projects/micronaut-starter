package io.micronaut.starter.feature.aws

import groovy.xml.XmlParser
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.awsalexa.AwsAlexa
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.core.template.Template
import spock.lang.PendingFeature
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
        def output = generate(createOptions(buildTool).applicationType(ApplicationType.DEFAULT).features([Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA]).build())

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

    void 'Function AppStack log retention is included for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(createOptions(buildTool).applicationType(ApplicationType.FUNCTION).features([Cdk.NAME]).build())

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.logs.RetentionDays;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.logRetention(RetentionDays.ONE_WEEK)')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.tracing(Tracing.ACTIVE)')

        where:
        buildTool << BuildTool.values()
    }

    void 'architecture defaults to X86 for  #buildTool'(BuildTool buildTool) {
        when:
        Options options = createOptions(buildTool).applicationType(ApplicationType.FUNCTION).features([Cdk.NAME]).build()
        Map<String, String> output = generate(options)

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains(".runtime(Runtime.JAVA_${options.javaVersion().majorVersion()})")
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.architecture(Architecture.X86_64)')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Architecture;')

        where:
        buildTool << BuildTool.values()
    }


    void "dependencies are added for cdk to infra project for #buildTool"(BuildTool buildTool, String buildFile) {
        when:
        Map<String, String> output = generate(createOptions(buildTool).applicationType(ApplicationType.DEFAULT).features([Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA]).build())
        String infraBuild = output."$Cdk.INFRA_MODULE/$buildFile"

        then:
        infraBuild.contains($/implementation("io.micronaut.starter:micronaut-starter-aws-cdk/$)

        where:
        buildTool               | buildFile
        BuildTool.GRADLE        | "build.gradle"
        BuildTool.GRADLE_KOTLIN | "build.gradle.kts"
    }

    void 'Function AppStack with Alexa Skills is included for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(createOptions(buildTool).applicationType(ApplicationType.FUNCTION).features([Cdk.NAME,AwsAlexa.NAME]).build())

        then:
        // aws-lambda is automatic, but if that assumption changes might need to fix cdkappstack rocker file
        output.'micronaut-cli.yml'.contains('aws-lambda')

        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.iam.ServicePrincipal;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Permission;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains("""
        function.addPermission("alexa-skills-kit-trigger", Permission.builder()
                .principal(new ServicePrincipal("alexa-appkit.amazon.com"))
                .eventSourceToken("Replace-With-SKILL-ID")
                .build());
""")

        where:
        buildTool << BuildTool.values()
    }

    void "dependencies are added for cdk to infra project for maven"() {
        when:
        Map<String, String> output = generate(createOptions(BuildTool.MAVEN).applicationType(ApplicationType.DEFAULT).features([Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA]).build())
        def dependency = new XmlParser().parseText(output."$Cdk.INFRA_MODULE/pom.xml").dependencies.dependency.find {
            it.artifactId.text() == 'micronaut-starter-aws-cdk'
        }

        then:
        with(dependency) {
            it.groupId.text() == 'io.micronaut.starter'
        }
    }

    private static MicronautOptions.Builder createOptions(BuildTool buildTool) {
        MicronautOptions.builder()
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(AwsLambdaFeatureValidator.firstSupportedJdk())
    }
}
