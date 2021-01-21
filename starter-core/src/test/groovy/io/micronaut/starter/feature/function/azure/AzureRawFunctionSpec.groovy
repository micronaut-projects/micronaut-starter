package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class AzureRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature azure-function contains links to docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(ApplicationType.FUNCTION, options, ['azure-function'])
        def readme = output["README.md"]

        then:
        readme
        verifyAll {
            readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions")
            !readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#azureHttpFunctions")
            readme.contains("https://docs.microsoft.com/azure")
            // don't add azure-function-http for ApplicationType.FUNCTION
            !readme.contains("## Feature azure-function-http documentation")
        }

        when:
        readme = readme.replaceFirst("## Feature azure-function documentation","")

        then:
        // make sure we didn't add docs more than once
        !readme.contains("## Feature azure-function documentation")
    }
}
