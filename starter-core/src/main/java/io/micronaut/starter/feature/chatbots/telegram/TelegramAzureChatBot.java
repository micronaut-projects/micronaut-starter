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
package io.micronaut.starter.feature.chatbots.telegram;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.chatbots.telegram.template.azureReadme;
import io.micronaut.starter.feature.function.azure.AzureBuildCommandUtils;
import io.micronaut.starter.feature.function.azure.AzureCloudFeature;
import io.micronaut.starter.feature.function.azure.AzureMicronautRuntimeFeature;
import io.micronaut.starter.feature.function.azure.AzureRawFunction;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Azure Functions.
 *
 * @author Tim Yates
 * @since 4.3.0
 */
@Singleton
public class TelegramAzureChatBot extends ChatBotsTelegram implements AzureCloudFeature, AzureMicronautRuntimeFeature {

    public static final String NAME = "chatbots-telegram-azure-function";

    public static final Dependency CHATBOTS_TELEGRAM_AZURE_FUNCTION = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-telegram-azure-function")
            .compile()
            .build();

    private final AzureRawFunction azureRawFunction;

    public TelegramAzureChatBot(MicronautValidationFeature validationFeature, AzureRawFunction azureRawFunction) {
        super(validationFeature);
        this.azureRawFunction = azureRawFunction;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.FUNCTION;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Telegram ChatBot as an Azure Function";
    }

    @Override
    public String getDescription() {
        return "Generates an application that can be deployed as an Azure Function that implements a Telegram ChatBot";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(AzureRawFunction.class, azureRawFunction);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addMicronautRuntimeBuildProperty(generatorContext);
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return AzureBuildCommandUtils.getBuildCommand(buildTool);
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_TELEGRAM_AZURE_FUNCTION);
    }

    @Override
    public String rootReadMeTemplate(GeneratorContext generatorContext) {
        return azureReadme.class.getName().replace(".", "/") + ".rocker.raw";
    }
}
