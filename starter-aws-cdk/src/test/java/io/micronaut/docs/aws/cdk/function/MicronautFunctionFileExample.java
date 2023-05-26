package io.micronaut.docs.aws.cdk.function;

import io.micronaut.aws.cdk.function.MicronautFunctionFile;
import io.micronaut.starter.options.BuildTool;

public class MicronautFunctionFileExample {
    String filename =
//tag::micronautFunctionFile[]
    MicronautFunctionFile.builder()
            .buildTool(BuildTool.GRADLE)
            .archiveBaseName("app")
            .graalVMNative()
            .version("0.1")
            .build();
//end::micronautFunctionFile[]
}
