package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class AzureRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature azure-function and Maven does not contain link to Azure Gradle plugin'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, ['azure-function'])
        String readme = output["README.md"]

        then:
        readme
        !readme.contains("The application's build uses [Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).")

        and: 'but contains link to CLI'
        readme.contains('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)')
    }

    void 'test readme.md with feature azure-function contains links to docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, ['azure-function'])
        String readme = output["README.md"]

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

        and: 'contains link to Azure CLI'
        readme.contains('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)')

        and: 'link to Azure Gradle Plugin'
        readme.contains("The application's build uses [Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).")

    }
}
