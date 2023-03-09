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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.chatbots.ChatbotsAwsLambda;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.chatbots.basecamp.BasecampUtils.BASECAMP_CHATBOTS;

@Singleton
public class BasecampAwsLambda extends ChatbotsAwsLambda {
    private static final String NAME = "chatbots-basecamp-lambda";
    private static final String ARTIFACT_ID_CHATBOTS_BASECAMP_LAMBDA = "micronaut-chatbots-basecamp-lambda";
    private static final Dependency DEPENDENCY_CHATBOTS_BASECAMP_LAMBDA = MicronautDependencyUtils.chatbotsDependency()
            .artifactId(ARTIFACT_ID_CHATBOTS_BASECAMP_LAMBDA)
            .compile()
            .build();
    private static final String HANDLER = "io.micronaut.chatbots.basecamp.lambda.Handler";

    protected BasecampAwsLambda(AwsLambda awsLambda) {
        super(awsLambda);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Chatbots Basecamp AWS Lambda";
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
        generatorContext.addDependency(DEPENDENCY_CHATBOTS_BASECAMP_LAMBDA);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/index.html#basecampLambda";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return BASECAMP_CHATBOTS;
    }

    @Override
    @NonNull
    public String handlerClass(@NonNull ApplicationType applicationType,  @NonNull Project project) {
        return HANDLER;
    }
}
