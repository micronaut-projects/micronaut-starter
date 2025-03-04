package io.micronaut.starter.feature.function.azure

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class AzureRawFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature azure-function and Maven does not contain link to Azure Gradle plugin'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(JdkVersion.JDK_8)
                .features(['azure-function'])
                .build()
        when:
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme
        !readme.contains("The application's build uses [Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).")

        and: 'but contains link to CLI'
        readme.contains('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)')
    }

    void 'test readme.md with feature azure-function contains links to docs'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(JdkVersion.JDK_8)
                .features(['azure-function'])
                .build()
        when:
        Map<String, String> output = generate(options)
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
