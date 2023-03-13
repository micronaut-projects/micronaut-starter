/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.config.NestedConfiguration;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Add support for Micronaut Data Azure Cosmos.
 */
@Singleton
public class DataAzureCosmosFeature implements DataDocumentFeature {
    private static final String NAME = "data-azure-cosmos";

    private static final String MICRONAUT_DATA_AZURE_COSMOS_DATA_ARTIFACT = "micronaut-data-azure-cosmos";
    private static final Dependency DEPENDENCY_MICRONAUT_DATA_AZURE_COSMOS_DATA = MicronautDependencyUtils.dataDependency()
            .artifactId(MICRONAUT_DATA_AZURE_COSMOS_DATA_ARTIFACT)
            .versionProperty(MICRONAUT_DATA_VERSION)
            .compile()
            .build();

    private static final String CONFIGURATION_PREFIX_AZURE_COSMOS = "azure.cosmos";

    private final Data data;

    protected DataAzureCosmosFeature(Data data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for defining data repositories for Azure Cosmos Db";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Azure Cosmos";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        // TODO: Add test resources/containers support
        generatorContext.getConfiguration().addNested(getConfiguration(generatorContext));
        getDependencies(generatorContext).forEach(generatorContext::addDependency);
    }

    protected NestedConfiguration getConfiguration(GeneratorContext generatorContext) {
        Map<String, Object> databaseProperties = CollectionUtils.mapOf(
                "database-name", "myDb",
                "update-policy", "NONE");
        return new NestedConfiguration(
                CONFIGURATION_PREFIX_AZURE_COSMOS,
                CollectionUtils.mapOf(
                "consistency-level", "SESSION",
                "default-gateway-mode", true,
                "endpoint-discovery-enabled", false,
                "database", databaseProperties));
    }

    protected List<Dependency> getDependencies(GeneratorContext generatorContext) {
        return (generatorContext.getBuildTool() == BuildTool.MAVEN) ?
                Arrays.asList(
                        DEPENDENCY_MICRONAUT_DATA_DOCUMENT_PROCESSOR,
                        DEPENDENCY_MICRONAUT_DATA_AZURE_COSMOS_DATA,
                        DEPENDENCY_MICRONAUT_DATA_PROCESSOR) :
                Arrays.asList(
                        DEPENDENCY_MICRONAUT_DATA_DOCUMENT_PROCESSOR,
                        DEPENDENCY_MICRONAUT_DATA_AZURE_COSMOS_DATA);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#azureCosmos";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://learn.microsoft.com/en-us/azure/cosmos-db/";
    }

}
