/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.camunda;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class Zeebe implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "zeebe";
    }

    @Override
    public String getTitle() {
        return "Zeebe Worker";
    }

    @Override
    public boolean isCommunity() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Bringing cloud native process automation to Micronaut: Implement Zeebe Workers for Camunda Cloud or a local broker";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("micronaut-zeebe-client-feature")
                .compile());
    }

    @Override
    public String getCategory() {
        return Category.BPM;
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://github.com/camunda-community-hub/micronaut-zeebe-client";
    }

}
