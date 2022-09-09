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
import io.micronaut.starter.feature.oraclecloud.OracleCloudSdk;
import jakarta.inject.Singleton;

/**
 * Oracle Cloud implementation of {@link ObjectStorageFeature}.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 3.7.0
 */
@Singleton
public class ObjectStorageOracleCloud implements ObjectStorageFeature {

    private final OracleCloudSdk oracleCloudSdk;

    public ObjectStorageOracleCloud(OracleCloudSdk oracleCloudSdk) {
        this.oracleCloudSdk = oracleCloudSdk;
    }

    @Override
    @NonNull
    public String getCloudProvider() {
        return "Oracle Cloud";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(OracleCloudSdk.class)) {
            featureContext.addFeature(oracleCloudSdk);
        }
    }

}
