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
package io.micronaut.starter.feature.json;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;

public interface SerializationFeature extends JsonFeature {

    @Override
    default boolean isPreview() {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.API;
    }

    @Override
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-serialization/1.0.x/guide/";
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

}
