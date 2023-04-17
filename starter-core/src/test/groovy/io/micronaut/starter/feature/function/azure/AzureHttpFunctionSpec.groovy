package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
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

    void 'test readme.md with feature azure-function contains links to docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(ApplicationType.DEFAULT, options, ['azure-function'])
        def readme = output["README.md"]

        then:
        readme
        verifyAll {
            readme.contains("# Micronaut and Azure Function")
            readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions")
            readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#azureHttpFunctions")
            readme.contains("https://docs.microsoft.com/azure")
        }

        when:
        readme = readme.replaceFirst("# Micronaut and Azure Function","")
        readme = readme.replaceFirst("## Feature azure-function documentation","")
        readme = readme.replaceFirst("## Feature azure-function-http documentation","")

        then:
        verifyAll {
            // make sure we didn't add anything more than once
            !readme.contains("# Micronaut and Azure Function")
            !readme.contains("## Feature azure-function documentation")
            !readme.contains("## Feature azure-function-http documentation")
        }

        and: 'Readme contains a link to the Gradle Plugin'
        readme.contains("[Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions)")

        and: 'contains link to Azure CLI'
        readme.contains('- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)')

        and: 'link to Azure Gradle Plugin'
        readme.contains("The application's build uses [Azure Functions Plugin for Gradle](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).")
    }

    void 'test gradle azure-function-http feature for language=#language and buildTool=#buildTool'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['azure-function-http'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        if (buildTool.isGradle()) {
            assert !verifier.hasDependency("io.micronaut.azure", "micronaut-azure-function-http", Scope.COMPILE)
            assert !verifier.hasDependency("io.micronaut.azure", "micronaut-azure-function-http-test", Scope.TEST)
            assert !verifier.hasDependency("com.microsoft.azure.functions", "azure-functions-java-library")

        } else if (buildTool == BuildTool.MAVEN) {
            assert verifier.hasDependency("io.micronaut.azure", "micronaut-azure-function-http", Scope.COMPILE)
            assert verifier.hasDependency("io.micronaut.azure", "micronaut-azure-function-http-test", Scope.TEST)
            assert verifier.hasDependency("com.microsoft.azure.functions", "azure-functions-java-library")
        }

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }
}
