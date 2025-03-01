package io.micronaut.starter.feature.architecture

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.projectgen.core.feature.OneOfFeature
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import spock.lang.Subject

class X86Spec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    X86 x86 = beanContext.getBean(X86)

    void 'x86 feature is in the cloud category'() {
        expect:
        x86.category == Category.CLOUD
    }

    void 'x86 feature is an instance of AwsLambdaEventFeature'() {
        expect:
        x86 instanceof CpuArchitecture
        x86 instanceof OneOfFeature
    }

    void "x86 supports every application type"(ApplicationType applicationType) {
        expect:
        x86.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'x86 plus cdk feature sets lambda function architecture for #buildTool'() {
        when:
        Options options = MicronautOptions.builder()
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(AwsLambdaFeatureValidator.firstSupportedJdk())
                .build()

        Map<String, String> output = generate(ApplicationType.FUNCTION, options,
                [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA, X86.NAME])

        then:
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/import software.amazon.awscdk.services.lambda.Architecture/$)
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/.architecture(Architecture.X86_64)/$)

        when: 'x86 is the default'
        output = generate(ApplicationType.FUNCTION, options,
                [Cdk.NAME, AwsLambda.FEATURE_NAME_AWS_LAMBDA])

        then:
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/import software.amazon.awscdk.services.lambda.Architecture/$)
        output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java".contains($/.architecture(Architecture.X86_64)/$)

        where:
        buildTool << BuildTool.valuesGradle()
    }
}
