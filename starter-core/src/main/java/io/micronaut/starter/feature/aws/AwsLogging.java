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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.Category.CLOUD;

@Singleton
public class AwsLogging implements AwsFeature {

    private static final Dependency AWS_LOGGING_DEPENDENCY =
            MicronautDependencyUtils.awsDependency()
                    .artifactId("micronaut-aws-cloudwatch-logging")
                    .compile()
                    .build();

    public static final String NAME = "amazon-logging";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Amazon Logging";
    }

    @Override
    public String getDescription() {
        return "Provides integration with Amazon CloudWatch Logs";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/#cloudWatchLogging";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html";
    }

    @Override
    public String getCategory() {
        return CLOUD;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(AWS_LOGGING_DEPENDENCY);
    }
}
