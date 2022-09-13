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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

@Singleton
public class AmazonApiGatewayHttp extends AwsLambdaRelatedFeature implements AwsApiFeature {
    public static final String NAME = "amazon-api-gateway-http";

    public AmazonApiGatewayHttp(AwsLambda awsLambda) {
        super(awsLambda);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Amazon API Gateway HTTP";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "This features combines with the CDK to define an API Gateway HTTP API";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://aws.amazon.com/api-gateway/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT ||
               applicationType == ApplicationType.FUNCTION;
    }
}
