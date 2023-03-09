/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.chatbots;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.RequiresMavenLocal;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.azure.AzureBuildPluginFeature;
import io.micronaut.starter.feature.function.azure.AzureMicronautRuntimeFeature;
import io.micronaut.starter.feature.gcp.GcpFeature;

public abstract class ChatbotsAzureFunction extends GcpFeature implements ChatBotsFeature, FunctionFeature, AzureMicronautRuntimeFeature, RequiresMavenLocal {

    private final AzureBuildPluginFeature azureBuildPluginFeature;

    protected ChatbotsAzureFunction(AzureBuildPluginFeature azureBuildPluginFeature) {
        this.azureBuildPluginFeature = azureBuildPluginFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(AzureBuildPluginFeature.class, azureBuildPluginFeature);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addMicronautRuntimeBuildProperty(generatorContext);
        addDependencies(generatorContext);
    }

    protected abstract void addDependencies(@NonNull GeneratorContext generatorContext);

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/index.html";
    }

    @Override
    public String getCategory() {
        return ChatBotsFeature.super.getCategory();
    }
}
