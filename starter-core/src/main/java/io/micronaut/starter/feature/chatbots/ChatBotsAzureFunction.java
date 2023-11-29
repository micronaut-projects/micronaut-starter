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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.azure.AzureMicronautRuntimeFeature;
import io.micronaut.starter.feature.function.azure.AzureRawFunction;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;

/**
 * Base class for Azure Functions chatbot features.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
public abstract class ChatBotsAzureFunction extends ChatBots implements CloudFeature, AzureMicronautRuntimeFeature {

    private final AzureRawFunction azureRawFunction;

    protected ChatBotsAzureFunction(MicronautValidationFeature validationFeature, AzureRawFunction azureRawFunction) {
        super(validationFeature);
        this.azureRawFunction = azureRawFunction;
    }

    @Override
    public Cloud getCloud() {
        return Cloud.AZURE;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(AzureRawFunction.class, azureRawFunction);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addMicronautRuntimeBuildProperty(generatorContext);
        addDependencies(generatorContext);
        addConfigurations(generatorContext);
        renderTemplates(generatorContext);
    }

    /**
     * Add dependencies to the project.
     *
     * @param generatorContext The generator context
     */
    protected abstract void addDependencies(@NonNull GeneratorContext generatorContext);
}
