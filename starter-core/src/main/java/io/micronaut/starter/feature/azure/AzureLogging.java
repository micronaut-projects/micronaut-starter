/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.function.azure.AzureCloudFeature;
import jakarta.inject.Singleton;
import static io.micronaut.starter.feature.Category.LOGGING;

@Requires(property = "micronaut.starter.feature.azure.logging.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureLogging implements AzureCloudFeature, Feature {

    public static final String NAME = "azure-logging";

    private static final String ARTIFACT_ID_MICRONAUT_AZURE_LOGGING = "micronaut-azure-logging";
    private static final Dependency AZURE_LOGGING_DEPENDENCY =
            MicronautDependencyUtils.azureDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_AZURE_LOGGING)
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Azure Logging";
    }

    @Override
    public String getDescription() {
        return "Provides integration with Azure Monitor Logs";
    }

    @Nullable
    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureLogging";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://learn.microsoft.com/en-us/azure/azure-monitor/logs/data-platform-logs";
    }

    @Override
    public String getCategory() {
        return LOGGING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(AZURE_LOGGING_DEPENDENCY);
    }
}
