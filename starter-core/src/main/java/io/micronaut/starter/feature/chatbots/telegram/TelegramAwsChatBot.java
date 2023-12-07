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
package io.micronaut.starter.feature.chatbots.telegram;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.aws.AwsFeature;
import io.micronaut.starter.feature.aws.AwsMicronautRuntimeFeature;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.chatbots.template.awsCdkReadme;
import io.micronaut.starter.feature.chatbots.template.awsReadme;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

/**
 * Adds support for Telegram chatbots as Lambdas.
 *
 * @author Tim Yates
 * @since 4.3.0
 */
@Singleton
public class TelegramAwsChatBot extends ChatBotsTelegram implements AwsFeature, AwsMicronautRuntimeFeature, HandlerClassFeature {

    public static final String NAME = "chatbots-telegram-lambda";

    public static final Dependency CHATBOTS_TELEGRAM_LAMBDA = MicronautDependencyUtils
            .chatBotsDependency()
            .artifactId("micronaut-chatbots-telegram-lambda")
            .compile()
            .build();

    public static final String HANDLER_CLASS = "io.micronaut.chatbots.telegram.lambda.Handler";
    private final AwsLambda awsLambda;

    public TelegramAwsChatBot(MicronautValidationFeature validationFeature, AwsLambda awsLambda) {
        super(validationFeature);
        this.awsLambda = awsLambda;
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
        return "Telegram ChatBot as AWS Lambda function";
    }

    @Override
    public String getDescription() {
        return "Generates an application that can be deployed as an AWS Lambda function that implements a Telegram ChatBot";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(AwsLambda.class, awsLambda);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addMicronautRuntimeBuildProperty(generatorContext);
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(CHATBOTS_TELEGRAM_LAMBDA);
    }

    @Override
    public String rootReadMeTemplate(GeneratorContext generatorContext) {
        return generatorContext.isFeaturePresent(Cdk.class) ?
            awsCdkReadme.class.getName().replace(".", "/") + ".rocker.raw" :
            awsReadme.class.getName().replace(".", "/") + ".rocker.raw";
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return "";
    }

    @Override
    public String handlerClass(ApplicationType applicationType, Project project) {
        return HANDLER_CLASS;
    }
}
