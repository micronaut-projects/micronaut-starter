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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class AwsLambdaEventsSerde implements AwsFeature {
    public static final String NAME = "aws-lambda-events-serde";

    private static final Dependency DEPENDENCY_MICRONAUT_AWS_LAMBDA_EVENTS_SERDE = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-aws-lambda-events-serde")
            .compile()
            .version("4.0.0-M8")
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Events Serde";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "It adds support for Micronaut Serialization with AWS Lambda Java Events.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_AWS_LAMBDA_EVENTS_SERDE);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/snapshot/guide/#eventsLambdaSerde";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events";
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }
}
