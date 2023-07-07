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
import io.micronaut.starter.feature.function.LambdaRuntimeMainClass;
import jakarta.inject.Singleton;

@Singleton
public class LambdaFunctionUrl extends CdkFeature implements AwsApiFeature, LambdaRuntimeMainClass {

    public static final String NAME = "aws-lambda-function-url";

    public LambdaFunctionUrl(Cdk cdk) {
        super(cdk);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function URLs";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "A function URL is a dedicated HTTP(S) endpoint for a Lambda function. This features combines with CDK to configure a function URL for Lambda";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/lambda/latest/dg/lambda-urls.html";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public String getLambdaRuntimeMainClass() {
        return "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";
    }
}

