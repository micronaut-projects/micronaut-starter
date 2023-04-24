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
package io.micronaut.starter.feature.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.KotlinSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class KotlinSymbolProcessing implements KotlinSpecificFeature {
    @Override
    @NonNull
    public String getName() {
        return "ksp";
    }

    @Override
    public String getCategory() {
        return Category.LANGUAGES;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Kotlin Symbol Processing (KSP)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for processing source code at compilation time with Kotlin Symbol Processing (KSP).";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/#kotlin";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://kotlinlang.org/docs/ksp-overview.html";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
