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

import java.util.Set;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

@Singleton
public class JacksonDatabindFeature implements JsonFeature, DefaultFeature {
    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(feature -> feature instanceof JsonFeature);
    }

    @Override
    public String getName() {
        return "jackson-databind";
    }    

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
            .compile()
            .groupId("io.micronaut")
            .artifactId("micronaut-jackson-databind")
            .build()
        );
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public String getDescription() {
        return "Adds Jackson Databind to a Micronaut Application";
    }

    @Override
    public String getTitle() {
        return "Jackson Databind Integration";
    }
}
