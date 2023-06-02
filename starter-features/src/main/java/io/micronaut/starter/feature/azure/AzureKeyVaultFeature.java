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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import jakarta.inject.Singleton;

/**
 * Azure Key Vault Feature.
 *
 * @author sbodvanski
 * @since 3.8.0
 */
@Singleton
public class AzureKeyVaultFeature implements DistributedConfigFeature {
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
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureKeyVault";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://azure.microsoft.com/en-us/services/key-vault/#product-overview";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.azure")
                .artifactId("micronaut-azure-secret-manager")
                .compile());
        populateBootstrapForDistributedConfiguration(generatorContext);
    }
}
