package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class AzureHttpFunctionSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature azure-function contains links to micronaut docs'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8)
        def output = generate(ApplicationType.DEFAULT, options, ['azure-function'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-azure/snapshot/guide/index.html#azureHttpFunctions")
    }
}
