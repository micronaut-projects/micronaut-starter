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
package io.micronaut.starter.feature.azure;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.OneOfFeature;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Azure Cosmos DB Feature.
 *
 * @author radovanradic
 * @since 3.8.0
 */
@Requires(property = "micronaut.starter.feature.azure.cosmos.db.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureCosmosDbFeature implements OneOfFeature {

    @Override
    public String getName() {
        return "azure-cosmos-db";
    }

    @Override
    public String getTitle() {
        return "Azure Cosmos DB";
    }

    @Override
    public String getDescription() {
        return "Enables access to Azure Cosmos DB";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public Class<?> getFeatureClass() {
        return AzureCosmosDbFeature.class;
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureCosmosClient";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://learn.microsoft.com/en-us/azure/cosmos-db/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.azure")
                .artifactId("micronaut-azure-cosmos")
                .compile());
        Map<String, Object> properties = new HashMap<>(5);
        properties.put("consistency-level", "SESSION");
        properties.put("endpoint", "azure-cosmos-endpoint");
        properties.put("key", "");
        properties.put("default-gateway-mode", true);
        properties.put("endpoint-discovery-enabled", false);
        generatorContext.getConfiguration().addNested("azure.cosmos", properties);
    }
}
