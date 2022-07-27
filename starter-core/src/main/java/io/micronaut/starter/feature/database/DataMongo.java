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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

/**
 * Add support for Micronaut Data MongoDB. 
 
 * @author graemerocher
 */
@Singleton
public class DataMongo extends DataMongoFeature {

    private static final String SYNC_MONGODB_ARTIFACT = "mongodb-driver-sync";

    public DataMongo(Data data, TestContainers testContainers, TestResources testResources) {
        super(data, testContainers, testResources);
    }

    @NonNull
    @Override
    public String getName() {
        return "data-mongodb";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for defining synchronous data repositories for MongoDB";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data MongoDB";
    }

    @NonNull
    @Override
    protected String mongoArtifact() {
        return SYNC_MONGODB_ARTIFACT;
    }
}
