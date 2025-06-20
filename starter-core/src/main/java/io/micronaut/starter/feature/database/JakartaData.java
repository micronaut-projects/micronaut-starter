/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class JakartaData implements Feature {
    private static final Dependency DEPENDENCY_JAKARTA_DATA_API = Dependency.builder()
            .groupId("jakarta.data")
            .artifactId("jakarta-data-api")
            .compile()
            .build();

    @Override
    public String getName() {
        return "jakarta-data";
    }

    @Override
    public String getTitle() {
        return "Jakarta Data";
    }

    @Override
    public String getDescription() {
        return "Adds the Jakarta Data API dependency to your project.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_JAKARTA_DATA_API);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#jakartaData";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://jakarta.ee/specifications/data/1.0/jakarta-data-1.0";
    }

}
