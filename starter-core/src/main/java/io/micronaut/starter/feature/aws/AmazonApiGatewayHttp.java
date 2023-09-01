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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.LambdaRuntimeMainClass;
import io.micronaut.starter.feature.function.awslambda.ApiGatewayV2AwsLambdaHandlerProvider;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

@Singleton
public class AmazonApiGatewayHttp extends AwsLambdaRelatedFeature implements AwsApiFeature, LambdaRuntimeMainClass {
    public static final String NAME = "amazon-api-gateway-http";

    private final ApiGatewayV2AwsLambdaHandlerProvider apiGatewayV2AwsLambdaHandlerProvider;

    public AmazonApiGatewayHttp(AwsLambda awsLambda, ApiGatewayV2AwsLambdaHandlerProvider apiGatewayV2AwsLambdaHandlerProvider) {
        super(awsLambda);
        this.apiGatewayV2AwsLambdaHandlerProvider = apiGatewayV2AwsLambdaHandlerProvider;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeature(apiGatewayV2AwsLambdaHandlerProvider);
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
    public String getLambdaRuntimeMainClass() {
        return "io.micronaut.function.aws.runtime.APIGatewayV2HTTPEventMicronautLambdaRuntime";
    }
}
