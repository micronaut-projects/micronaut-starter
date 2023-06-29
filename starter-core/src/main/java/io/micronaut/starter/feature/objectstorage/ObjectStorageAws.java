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
package io.micronaut.starter.feature.objectstorage;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.aws.AwsV2Sdk;
import jakarta.inject.Singleton;

/**
 * AWS implementation of {@link ObjectStorageFeature}.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 3.7.0
 */
@Singleton
public class ObjectStorageAws implements CloudObjectStorageFeature, AwsCloudFeature {

    private final AwsV2Sdk awsV2Sdk;

    public ObjectStorageAws(AwsV2Sdk awsV2Sdk) {
        this.awsV2Sdk = awsV2Sdk;
    }

    @Override
    @NonNull
    public String getCloudProvider() {
        return getCloud().name();
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(AwsV2Sdk.class)) {
            featureContext.addFeature(awsV2Sdk);
        }
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://aws.amazon.com/s3/";
    }
}
