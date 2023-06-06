package io.micronaut.aws.cdk.function

import io.micronaut.starter.options.BuildTool
import spock.lang.Specification
import spock.lang.Unroll

class MicronautFunctionFileSpec extends Specification {

    @Unroll
    void "micronaut function file generated scenario"(BuildTool buildTool, boolean optimized, boolean graalVMNative, String archiveBase, String version, String expected) {
        given:
        MicronautFunctionFile.Builder builder = MicronautFunctionFile.builder().archiveBaseName(archiveBase)
        if (buildTool) {
            builder = builder.buildTool(buildTool)
        }
        if (optimized) {
            builder = builder.optimized()
        }
        if (graalVMNative) {
            builder = builder.graalVMNative()
        }
        if (version) {
            builder = builder.version(version)
        }

        expect:
        expected == builder.build()

        where:
        buildTool               | optimized | graalVMNative | archiveBase | version    | expected
        null                    | false     | false         | 'app'       | '0.1'      | 'app-0.1-all.jar'
        BuildTool.GRADLE_KOTLIN | false     | false         | 'app'       | '0.1'      | 'app-0.1-all.jar'
        BuildTool.GRADLE_KOTLIN | true      | false         | 'app'       | '0.1'      | 'app-0.1-all-optimized.jar'
        BuildTool.GRADLE        | false     | false         | 'app'       | '0.1'      | 'app-0.1-all.jar'
        BuildTool.GRADLE        | true      | false         | 'app'       | '0.1'      | 'app-0.1-all-optimized.jar'
        BuildTool.MAVEN         | false     | false         | 'app'       | '0.1'      | 'app-0.1.jar'
        BuildTool.MAVEN         | true      | false         | 'app'       | '0.1'      | 'app-0.1.jar'
        BuildTool.GRADLE_KOTLIN | false     | true          | 'app'       | '0.1'      | 'app-0.1-lambda.zip'
        BuildTool.GRADLE_KOTLIN | true      | true          | 'app'       | '0.1'      | 'app-0.1-optimized-lambda.zip'
        BuildTool.GRADLE        | false     | true          | 'app'       | '0.1'      | 'app-0.1-lambda.zip'
        BuildTool.GRADLE        | true      | true          | 'app'       | '0.1'      | 'app-0.1-optimized-lambda.zip'
        BuildTool.MAVEN         | false     | true          | 'app'       | '0.1'      | 'function.zip'
        BuildTool.MAVEN         | true      | true          | 'app'       | '0.1'      | 'function.zip'
        BuildTool.GRADLE_KOTLIN | false     | false         | 'app'       | null       | 'app-all.jar'
        BuildTool.GRADLE_KOTLIN | true      | false         | 'app'       | null       | 'app-all-optimized.jar'
        BuildTool.GRADLE        | false     | false         | 'app'       | null       | 'app-all.jar'
        BuildTool.GRADLE        | true      | false         | 'app'       | null       | 'app-all-optimized.jar'
        BuildTool.MAVEN         | false     | false         | 'app'       | null       | 'app.jar'
        BuildTool.MAVEN         | true      | false         | 'app'       | null       | 'app.jar'
        BuildTool.GRADLE_KOTLIN | false     | true          | 'app'       | null       | 'app-lambda.zip'
        BuildTool.GRADLE_KOTLIN | true      | true          | 'app'       | null       | 'app-optimized-lambda.zip'
        BuildTool.GRADLE        | false     | true          | 'app'       | null       | 'app-lambda.zip'
        BuildTool.GRADLE        | true      | true          | 'app'       | null       | 'app-optimized-lambda.zip'
        BuildTool.MAVEN         | false     | true          | 'app'       | null       | 'function.zip'
        BuildTool.MAVEN         | true      | true          | 'app'       | null       | 'function.zip'
    }
}
