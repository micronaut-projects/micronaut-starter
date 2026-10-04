package io.micronaut.starter.feature.distributedconfig

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class PythonDistributedConfigSpec extends BeanContextSpec implements CommandOutputFixture {

    void "Python #feature generates its dependency and native configuration import"(String feature, String dependency, String configImport) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                [feature]
        )

        then:
        output["pyproject.toml"].count('"' + dependency + '"') == 1
        output["config/application.toml"].contains("[micronaut.application]\nname = 'foo'")
        output["config/application.toml"].contains("[micronaut.config]\nimport = ['${configImport}']")
        !output["config/application.toml"].contains("config-client")
        !output.keySet().any { it.tokenize('/').last().startsWith('bootstrap') }
        !output.containsKey("src/main/resources/bootstrap.properties")
        !output.containsKey("src/main/resources/bootstrap.toml")

        where:
        feature               | dependency                                             | configImport
        "gcp-secrets-manager" | "io.micronaut.gcp:micronaut-gcp-secret-manager"         | "gcp-secret-manager://application?project-id=YOUR_PROJECT_ID"
        "azure-key-vault"     | "io.micronaut.azure:micronaut-azure-secret-manager"     | "azure-key-vault://YOUR_VAULT_NAME"
    }

    void "Python discovery-consul with #feature keeps all configuration out of bootstrap"(String feature) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["discovery-consul", feature]
        )

        then:
        output["config/application.toml"].contains("[consul.client]")
        output["config/application.toml"].contains("defaultZone = '\${CONSUL_HOST:localhost}:\${CONSUL_PORT:8500}'")
        output["config/application.toml"].contains("registration.enabled = true")
        output["config/application.toml"].contains("[micronaut.config]\nimport = ['")
        !output.keySet().any { it.tokenize('/').last().startsWith('bootstrap') }

        where:
        feature << ["gcp-secrets-manager", "azure-key-vault"]
    }

    void "Python Google and Azure features retain both native configuration imports"() {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(Language.PYTHON, TestFramework.PYTEST, BuildTool.PYRONAUT),
                ["gcp-secrets-manager", "azure-key-vault"]
        )

        then:
        output["config/application.toml"].contains("[micronaut.config]\nimport = [")
        output["config/application.toml"].count("gcp-secret-manager://application?project-id=YOUR_PROJECT_ID") == 1
        output["config/application.toml"].count("azure-key-vault://YOUR_VAULT_NAME") == 1
        output["pyproject.toml"].count('"io.micronaut.gcp:micronaut-gcp-secret-manager"') == 1
        output["pyproject.toml"].count('"io.micronaut.azure:micronaut-azure-secret-manager"') == 1
        !output.keySet().any { it.tokenize('/').last().startsWith('bootstrap') }
    }
}
