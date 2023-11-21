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
package io.micronaut.starter.feature.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

@Singleton
public class EclipseStoreRest implements EclipseStoreFeature {

    public static final String NAME = "eclipsestore-rest";
    public static final String ARTIFACT_ID_MICRONAUT_ECLIPSESTORE_REST = "micronaut-eclipsestore-rest";

    private final EclipseStore eclipseStore;

    public EclipseStoreRest(EclipseStore eclipseStore) {
        this.eclipseStore = eclipseStore;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "EclipseStore REST";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the EclipseStore REST API to your Micronaut project for development";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.eclipsestoreDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_ECLIPSESTORE_REST)
                .developmentOnly()
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(eclipseStore);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-eclipseStore/latest/guide/#rest";
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://docs.eclipsestore.io/manual/storage/rest-interface/index.html";
    }
}
