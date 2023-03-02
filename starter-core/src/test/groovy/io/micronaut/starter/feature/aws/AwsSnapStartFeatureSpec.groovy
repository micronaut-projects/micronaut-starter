package io.micronaut.starter.feature.aws


import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class AwsSnapStartFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    CoordinateResolver resolver = beanContext.getBean(CoordinateResolver)

    void 'SnapStart does not support ARM or Tracing #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, Arm.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.architecture(Architecture.X86_64)')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.tracing(Tracing.DISABLED)')

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN , BuildTool.MAVEN]
    }

    void 'Function AppStack imports included for SnapStart #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Alias;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.CfnFunction;')
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Version;')

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN , BuildTool.MAVEN]
    }

    void 'Function AppStack imports for SnapStart not included when using graalvm feature #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, GraalVM.FEATURE_NAME_GRAALVM])

        then:
        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Alias;')
        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.CfnFunction;')
        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('import software.amazon.awscdk.services.lambda.Version;')

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }

    void 'Function AppStack with SnapStart is included for #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('.memorySize(2048)')

        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains("""
        IConstruct defaultChild = function.getNode().getDefaultChild();
        if (defaultChild instanceof CfnFunction) {
            CfnFunction cfnFunction = (CfnFunction) defaultChild;
            cfnFunction.setSnapStart(CfnFunction.SnapStartProperty.builder()
                    .applyOn("PublishedVersions")
                    .build());
        }
""")
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('Version currentVersion = function.getCurrentVersion();')

        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains("""
        Alias prodAlias = Alias.Builder.create(this, "ProdAlias")
                .aliasName("Prod")
                .version(currentVersion)
                .build();
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "micronaut-function-api")
                .handler(prodAlias)
                .build();
""")

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN , BuildTool.MAVEN]
    }

    void 'Function AppStack with SnapStart not included when using graalvm feature #buildTool'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, GraalVM.FEATURE_NAME_GRAALVM])

        then:
        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains("""
        IConstruct defaultChild = function.getNode().getDefaultChild();
        if (defaultChild instanceof CfnFunction) {
            CfnFunction cfnFunction = (CfnFunction) defaultChild;
            cfnFunction.setSnapStart(CfnFunction.SnapStartProperty.builder()
                    .applyOn("PublishedVersions")
                    .build());
        }
""")
        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('Version currentVersion = function.getCurrentVersion();')

        !output.'infra/src/main/java/example/micronaut/AppStack.java'.contains("""
        Alias prodAlias = Alias.Builder.create(this, "ProdAlias")
                .aliasName("Prod")
                .version(currentVersion)
                .build();
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "micronaut-function-api")
                .handler(prodAlias)
                .build();
""")

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]
    }
}
