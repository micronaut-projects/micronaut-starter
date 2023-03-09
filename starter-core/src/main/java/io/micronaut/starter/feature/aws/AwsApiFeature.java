/*
 * Copyright 2017-2022 original authors
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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;

public interface AwsApiFeature extends AwsLambdaEventFeature, LambdaTrigger {
    Dependency MICRONAUT_AWS_APIGATEWAY = MicronautDependencyUtils.awsDependency()
            .artifactId("micronaut-aws-apigateway")
            .compile()
            .build();

    @Override
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway";
    }

    @Override
    default String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/apigateway/";
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT ||
                applicationType == ApplicationType.FUNCTION;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_AWS_APIGATEWAY);
    }
}
