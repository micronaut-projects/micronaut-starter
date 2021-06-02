package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*

class AzureHttpFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

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
    }
}
