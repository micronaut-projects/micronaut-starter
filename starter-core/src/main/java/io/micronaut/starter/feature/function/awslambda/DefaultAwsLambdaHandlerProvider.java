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
package io.micronaut.starter.feature.function.awslambda;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.aws.lambda.handler.default.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DefaultAwsLambdaHandlerProvider implements HandlerClassFeature {
    public static final String MICRONAUT_LAMBDA_HANDLER = "io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction";

    @Override
    @NonNull
    public String getName() {
        return "aws-lambda-handler-default";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.DEFAULT;
    }

    @Override
    @NonNull
    public String handlerClass(ApplicationType applicationType, Project project) {
        return MICRONAUT_LAMBDA_HANDLER;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOWEST.getOrder();
    }
}
