/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.awsapiproxy;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.function.FunctionFeature;
import javax.inject.Singleton;

@Singleton
public class AwsApiGatewayLambdaProxy implements FunctionFeature {

    public static final String FEATURE_NAME_AWS_API_GATEWAY_LAMBDA_PROXY = "aws-api-gateway-lambda-proxy";
    public static final String MAIN_CLASS_NAME = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";

    @NonNull
    @Override
    public String getName() {
        return FEATURE_NAME_AWS_API_GATEWAY_LAMBDA_PROXY;
    }

    public String getTitle() {
        return "AWS API Gateway Lambda Proxy";
    }

    @Override
    public String getDescription() {
        return "Deploy your application to AWS Lambda and use API Gateway to proxy requests to it. Code your application with Controllers.";
    }

    // Even we are flagging this as a function feature (to avoid the inclusion of things such as http-client), we allow this function to be run only in DEFAULT
    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public Category getCategory() {
        return Category.AWS;
    }
}
