package io.micronaut.docs.aws.cdk.function;

import io.micronaut.aws.cdk.function.MicronautFunction;
import io.micronaut.aws.cdk.function.MicronautFunctionFile;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Tracing;
import software.constructs.Construct;

public class AppStack extends Stack {
    public AppStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public AppStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

//tag::micronautFunctionDefault[]
        boolean graalVMNative = false;
        Function appFunction = MicronautFunction.create(ApplicationType.DEFAULT, graalVMNative, this, id)
                .architecture(Architecture.X86_64)
                .timeout(Duration.seconds(10))
                .memorySize(512)
                .tracing(Tracing.ACTIVE)
                .code(Code.fromAsset("../app/build/libs/" + MicronautFunctionFile.builder()
                        .buildTool(BuildTool.MAVEN)
                        .archiveBaseName("app")
                        .version("0.1")
                        .build()))
                .build();
//end::micronautFunctionDefault[]

//tag::micronautFunction[]
        Function.Builder builder = MicronautFunction.create(ApplicationType.FUNCTION, false, this, id)
                .handler("com.example.Handler");
//end::micronautFunction[]
    }
}
