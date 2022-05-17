package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime
import io.micronaut.starter.feature.function.CloudProvider
import io.micronaut.starter.feature.function.azure.AzureHttpFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction
import io.micronaut.starter.feature.function.oraclefunction.OracleFunction
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class CloudProviderSpec extends BeanContextSpec {

    void 'only #valid is a valid feature for #provider.name'() {
        given:
        def allFeatures = [
                GoogleCloudRawFunction.NAME,
                AwsLambdaCustomRuntime.NAME,
                AzureHttpFunction.NAME,
                OracleFunction.NAME,
        ]

        when:
        getFeatures(
                allFeatures,
                new Options(Language.JAVA, TestFramework.SPOCK, buildTool, JdkVersion.JDK_11, provider),
                ApplicationType.DEFAULT
        )

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Cannot use features ${(allFeatures - valid).sort()} when Cloud Provider is set to $provider.name"

        where:
        buildTool               | provider             | valid
        BuildTool.MAVEN         | CloudProvider.AWS    | AwsLambdaCustomRuntime.NAME
        BuildTool.MAVEN         | CloudProvider.GCP    | GoogleCloudRawFunction.NAME
        BuildTool.MAVEN         | CloudProvider.AZURE  | AzureHttpFunction.NAME
        BuildTool.MAVEN         | CloudProvider.ORACLE | OracleFunction.NAME
        BuildTool.GRADLE        | CloudProvider.AWS    | AwsLambdaCustomRuntime.NAME
        BuildTool.GRADLE        | CloudProvider.GCP    | GoogleCloudRawFunction.NAME
        BuildTool.GRADLE        | CloudProvider.AZURE  | AzureHttpFunction.NAME
        BuildTool.GRADLE        | CloudProvider.ORACLE | OracleFunction.NAME
        BuildTool.GRADLE_KOTLIN | CloudProvider.AWS    | AwsLambdaCustomRuntime.NAME
        BuildTool.GRADLE_KOTLIN | CloudProvider.GCP    | GoogleCloudRawFunction.NAME
        BuildTool.GRADLE_KOTLIN | CloudProvider.AZURE  | AzureHttpFunction.NAME
        BuildTool.GRADLE_KOTLIN | CloudProvider.ORACLE | OracleFunction.NAME
    }

}
