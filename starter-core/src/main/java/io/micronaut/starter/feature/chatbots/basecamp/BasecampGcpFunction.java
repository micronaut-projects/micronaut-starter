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
package io.micronaut.starter.feature.chatbots.basecamp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.chatbots.ChatbotsGcpFunction;
import io.micronaut.starter.feature.function.gcp.GcpReadmeFeature;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.chatbots.basecamp.BasecampUtils.BASECAMP_CHATBOTS;

@Singleton
public class BasecampGcpFunction extends ChatbotsGcpFunction {
    private static final String NAME = "chatbots-basecamp-gcp-function";
    private static final String ARTIFACT_ID_CHATBOTS_BASECAMP_GCP_FUNCTION = "micronaut-chatbots-basecamp-gcp-function";
    private static final Dependency DEPENDENCY_CHATBOTS_BASECAMP_GCP_FUNCTION = MicronautDependencyUtils.chatbotsDependency()
            .artifactId(ARTIFACT_ID_CHATBOTS_BASECAMP_GCP_FUNCTION)
            .api()
            .build();

    public BasecampGcpFunction(GcpReadmeFeature gcpReadmeFeature) {
        super(gcpReadmeFeature);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Chatbots Basecamp Google Cloud Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Basecamp Chatbots deployed to an AWS Lambda Function";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        BasecampUtils.addHandler(generatorContext);
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_CHATBOTS_BASECAMP_GCP_FUNCTION);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/index.html#basecampGcp";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return BASECAMP_CHATBOTS;
    }

    @Override
    @NonNull
    public String entryPoint(@NonNull GeneratorContext generatorContext) {
        return "io.micronaut.chatbots.basecamp.googlecloud.Handler";
    }
}
