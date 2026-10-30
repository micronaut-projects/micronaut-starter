/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.distributedconfig

import io.micronaut.context.ApplicationContext
import io.micronaut.context.DefaultApplicationContextBuilder
import io.micronaut.context.env.ConfigImportPropertySourcesLocator
import io.micronaut.context.env.DefaultEnvironment
import io.micronaut.context.env.PropertySource
import io.micronaut.context.env.PropertySourceImporter
import io.micronaut.core.convert.value.ConvertibleValues
import io.micronaut.core.util.ConnectionString
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.toml.env.TomlPropertySourceLoader

import java.nio.charset.StandardCharsets

class DistributedConfigImportsSpec extends BeanContextSpec implements CommandOutputFixture {

    private static final Map<String, Object> AZURE_CREDENTIALS = [
            AZURE_VAULT_URI: "https://example.vault.azure.net/",
            AZURE_CLIENT_ID: "client-id",
            AZURE_TENANT_ID: "tenant-id",
            AZURE_SECRET_VALUE: "client-secret"]

    void "combined #features declarations render valid TOML and reach every importer"(List<String> features, List<String> providers) {
        given:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE), ["toml"] + features)

        when:
        Map<String, Object> config = new TomlPropertySourceLoader().read("application",
                new ByteArrayInputStream(output["src/main/resources/application.toml"].getBytes(StandardCharsets.UTF_8)))
        List<String> imported = []
        def locator = new ConfigImportPropertySourcesLocator({ providers.collect { importer(it, imported) } })
        def environment = new DefaultEnvironment((DefaultApplicationContextBuilder) ApplicationContext.builder())
        environment.addPropertySource(PropertySource.of("generated-application", config))
        environment.addPropertySource(PropertySource.of("azure-credentials", AZURE_CREDENTIALS,
                PropertySource.PropertyConvention.ENVIRONMENT_VARIABLE))
        locator.load(environment)

        then:
        imported.toSet() == providers.toSet()
        imported.size() == providers.size()
        !output.keySet().any { it.tokenize('/').last().startsWith('bootstrap') }
        !features.contains("discovery-consul") || config["consul.client.defaultZone"] == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'

        cleanup:
        locator?.close()
        environment?.close()

        where:
        features                                      | providers
        ["gcp-secrets-manager", "azure-key-vault"]      | ["gcp-secret-manager", "azure-key-vault"]
        ["aws-secrets-manager", "aws-parameter-store"] | ["aws-secretsmanager", "parameterstore"]
        ["azure-key-vault", "oracle-cloud-vault"]      | ["azure-key-vault", "oraclecloud-vault"]
        ["azure-key-vault"]                           | ["azure-key-vault"]
        ["oracle-cloud-vault"]                        | ["oraclecloud-vault"]
        ["gcp-secrets-manager", "discovery-consul"]    | ["gcp-secret-manager"]
        ["azure-key-vault", "discovery-consul"]        | ["azure-key-vault"]
    }

    private PropertySourceImporter importer(String provider, List<String> imported) {
        Stub(PropertySourceImporter) {
            getProvider() >> provider
            isEnabled() >> true
            newImportDeclaration(_ as ConnectionString) >> provider
            newImportDeclaration(_ as ConvertibleValues) >> provider
            importPropertySource(_ as PropertySourceImporter.ImportContext) >> {
                imported.add(provider)
                Optional.of(PropertySource.of("imported-" + provider, [:]))
            }
        }
    }
}
