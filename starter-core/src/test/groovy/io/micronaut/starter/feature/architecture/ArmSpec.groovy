package io.micronaut.starter.feature.architecture

import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.projectgen.core.feature.OneOfFeature
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Subject

class ArmSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    Arm arm = beanContext.getBean(Arm)

    void 'arm feature is in the cloud category'() {
        expect:
        arm.category == Category.CLOUD
    }

    void 'arm feature is an instance of AwsLambdaEventFeature'() {
        expect:
        arm instanceof CpuArchitecture
        arm instanceof OneOfFeature
    }

    void "arm supports every application type"(ApplicationType applicationType) {
        expect:
        arm.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
    }

    void 'arm plus cdk feature sets lambda function architecture for #buildTool'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA, Arm.NAME])
                .build()
        when:
        Map<String, String> output = generate(options)

        then:
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/import software.amazon.awscdk.services.lambda.Architecture/$)
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/.architecture(Architecture.ARM_64)/$)

        where:
        buildTool << [BuildTool.GRADLE]//BuildTool.valuesGradle()
    }
}
