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
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

@Singleton
public class AwsLambdaS3EventNotification extends AwsLambdaEventFunctionFeature implements LambdaTrigger {
    public static final String NAME = "aws-lambda-s3-event-notification";

    public AwsLambdaS3EventNotification(AwsLambda awsLambda) {
        super(awsLambda);
    }
    
    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public String getTitle() {
        return "AWS S3 Notification Event";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Creates a function handler that subscribes to a S3 notification event.";
    }
}
