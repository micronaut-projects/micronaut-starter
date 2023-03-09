package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class AzureHttpFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature azure-function and Maven does not contain link to Azure Gradle plugin'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_8)
        def output = generate(ApplicationType.DEFAULT, options, ['azure-function'])
        def readme = output["README.md"]

        then:
        readme
        !readme.contains("The application's build uses [Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).")

        and: 'but it contains link to Azure CLI'
        readme.contains('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)')
    }

    void 'test readme.md with feature azure-function contains links to docs'(ApplicationType applicationType,
                                                                             String micronautDocsUrl) {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        Map<String, String> output = generate(applicationType, options, ['azure-function'])
        String readme = output["README.md"]

        then:
        readme
        readme.count("# Micronaut and Azure Function") == 1
        readme.count(micronautDocsUrl)== 1
        readme.count("[https://docs.microsoft.com/azure](https://docs.microsoft.com/azure)") == 1
        readme.count("## Feature azure-function documentation")  == 1

        and: 'Readme contains a link to the Gradle Plugin'
        readme.count("[Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions)") == 1

        and: 'count link to Azure CLI'
        readme.count('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)') == 1

        where:
        applicationType          | micronautDocsUrl
        ApplicationType.FUNCTION | 'https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions'
        ApplicationType.DEFAULT  | 'https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#azureHttpFunctions'
    }
}
