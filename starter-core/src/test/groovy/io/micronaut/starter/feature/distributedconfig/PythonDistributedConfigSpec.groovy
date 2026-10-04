package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class PythonDistributedConfigSpec extends BeanContextSpec implements CommandOutputFixture {

    void "Python #feature generates its dependency and bootstrap TOML"(String feature, String dependency) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                [feature]
        )

        then:
        output["pyproject.toml"].count('"' + dependency + '"') == 1
        output["config/bootstrap.toml"].contains("[micronaut.application]\nname = 'foo'")
        output["config/bootstrap.toml"].contains("[micronaut.config-client]\nenabled = true")
        !output.containsKey("src/main/resources/bootstrap.properties")
        !output.containsKey("src/main/resources/bootstrap.toml")

        where:
        feature               | dependency
        "gcp-secrets-manager" | "io.micronaut.gcp:micronaut-gcp-secret-manager"
        "azure-key-vault"     | "io.micronaut.azure:micronaut-azure-secret-manager"
    }
}
