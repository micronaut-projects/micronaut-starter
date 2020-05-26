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
package io.micronaut.starter.feature.cache;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Caffeine implements Feature {

    @Override
    public String getName() {
        return "cache-caffeine";
    }

    @Override
    public String getTitle() {
        return "Caffeine Cache";
    }

    @Override
    public String getDescription() {
        return "Adds support for cache using Caffeine";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CACHE;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/ben-manes/caffeine";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html";
    }
}
