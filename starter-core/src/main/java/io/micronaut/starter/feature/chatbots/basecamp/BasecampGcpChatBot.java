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
package io.micronaut.starter.feature.chatbots.basecamp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.chatbots.basecamp.template.gcpReadme;
import io.micronaut.starter.feature.function.gcp.GcpCloudFeature;
import io.micronaut.starter.feature.function.gcp.GcpCloudFunctionBuildCommandUtils;
import io.micronaut.starter.feature.function.gcp.GcpMicronautRuntimeFeature;
import io.micronaut.starter.feature.function.gcp.GoogleCloudRawFunction;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Google Cloud Functions.
 *
 * @author Tim Yates
 * @since 4.3.0
 */
@Singleton
public class BasecampGcpChatBot extends ChatBotsBasecamp implements GcpCloudFeature, GcpMicronautRuntimeFeature {

    public static final String NAME = "chatbots-basecamp-gcp-function";

    public static final Dependency CHATBOTS_BASECAMP_GCP_FUNCTION = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-basecamp-gcp-function")
            .compile()
            .build();
    private final GoogleCloudRawFunction rawFunction;

    public BasecampGcpChatBot(MicronautValidationFeature validationFeature, GoogleCloudRawFunction rawFunction) {
        super(validationFeature);
        this.rawFunction = rawFunction;
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
        return "Basecamp ChatBot as a Google Cloud Function";
    }

    @Override
    public String getDescription() {
        return "Generates an application that can be deployed as a Google Cloud Function that implements a Basecamp ChatBot";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(GoogleCloudRawFunction.class, rawFunction);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addMicronautRuntimeBuildProperty(generatorContext);
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return GcpCloudFunctionBuildCommandUtils.getBuildCommand(buildTool);
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_BASECAMP_GCP_FUNCTION);
    }

    @Override
    public RockerTemplate rootReadMeTemplate(GeneratorContext generatorContext) {
        return new RockerTemplate(gcpReadme.template(generatorContext.getProject(), generatorContext.getFeatures(), getBuildCommand(generatorContext.getBuildTool())));
    }
}
