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
package io.micronaut.starter.feature.objectstorage;

import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class ObjectStorageLocal implements ObjectStorageFeature {
    private static final String LOCAL = "Local";
    private static final String DESCRIPTION = " This feature adds a local implementation to save to a folder in your computer which you may want to use during testing and development.";
    private static final String URL = "https://micronaut-projects.github.io/micronaut-object-storage/latest/guide/index.html#local";

    @Override
    public String getCloudProvider() {
        return LOCAL;
    }

    @Override
    public String getDescription() {
        return PREAMBLE + DESCRIPTION;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getMicronautDocumentation() {
        return URL;
    }
}
