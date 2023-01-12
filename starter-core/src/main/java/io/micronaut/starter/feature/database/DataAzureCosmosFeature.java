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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Add support for Micronaut Data Azure Cosmos.
 */
@Singleton
public class DataAzureCosmosFeature extends EaseTestingFeature implements DataFeature {

    private static final String MICRONAUT_DATA_GROUP = "io.micronaut.data";
    private static final String MICRONAUT_DATA_VERSION = "micronaut.data.version";
    private static final String MICRONAUT_DATA_PROCESSOR_ARTIFACT = "micronaut-data-processor";
    private static final String MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT = "micronaut-data-document-processor";
    private static final String MICRONAUT_DATA_AZURE_COSMOS_DATA_ARTIFACT = "micronaut-data-azure-cosmos";

    // These are just placeholder values for user to replace
    private static final String DEFAULT_ENDPOINT = "https://some-host/some-db";
    private static final String DEFAULT_KEY = "some-key";

    private final Data data;

    protected DataAzureCosmosFeature(Data data, TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
        this.data = data;
    }

    @NonNull
    @Override
    public String getName() {
        return "data-azure-cosmos";
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

        Map<String, Object> properties = new HashMap<>(5);
        properties.put("consistency-level", "SESSION");
        properties.put("endpoint", DEFAULT_ENDPOINT);
        properties.put("key", Base64.getEncoder().encodeToString(DEFAULT_KEY.getBytes()));
        properties.put("default-gateway-mode", true);
        properties.put("endpoint-discovery-enabled", false);
        Map<String, Object> databaseProperties = new HashMap<>(2);
        databaseProperties.put("database-name", "myDb");
        databaseProperties.put("update-policy", "NONE");
        properties.put("database", databaseProperties);
        generatorContext.getConfiguration().addNested("azure.cosmos", properties);

        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(Dependency.builder()
                    .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
                    .annotationProcessor(true)
                    .groupId(MICRONAUT_DATA_GROUP)
                    .artifactId(MICRONAUT_DATA_PROCESSOR_ARTIFACT)
                    .versionProperty(MICRONAUT_DATA_VERSION));
        }
        generatorContext.addDependency(Dependency.builder()
                .annotationProcessor()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_DATA_GROUP)
                .artifactId(MICRONAUT_DATA_AZURE_COSMOS_DATA_ARTIFACT)
                .versionProperty(MICRONAUT_DATA_VERSION));
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#azureCosmos";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeature(data);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://learn.microsoft.com/en-us/azure/cosmos-db/";
    }

}
