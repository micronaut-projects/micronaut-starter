/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.azure;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.features.config.MicronautDistributedConfigurationFeature;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.discovery.DiscoveryClient;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import jakarta.inject.Singleton;

/**
 * Azure Key Vault Feature.
 *
 * @author sbodvanski
 * @since 3.8.0
 */
@Requires(property = "micronaut.starter.feature.azure.key.vault.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureKeyVaultFeature implements MicronautDistributedConfigurationFeature {
    private static final String ARTIFACT_ID_MICRONAUT_AZURE_SECRET_MANAGER = "micronaut-azure-secret-manager";
    private static final Dependency KEY_VAULT_DEPENDENCY = MicronautDependencyUtils.azureDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_AZURE_SECRET_MANAGER)
            .compile()
            .build();
    private final DiscoveryClient discoveryClient;

    public AzureKeyVaultFeature(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DiscoveryClient.class)) {
            featureContext.addFeature(discoveryClient);
        }
    }

    @Override
    public String getTitle() {
        return "Azure Key Vault";
    }

    @Override
    public String getName() {
        return "azure-key-vault";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Azure Key Vault";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureKeyVault";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://azure.microsoft.com/en-us/services/key-vault/#product-overview";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        populateBootstrapForDistributedConfiguration(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(KEY_VAULT_DEPENDENCY);
    }
}
