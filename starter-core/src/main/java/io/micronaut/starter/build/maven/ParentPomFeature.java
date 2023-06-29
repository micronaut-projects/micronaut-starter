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
package io.micronaut.starter.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;

import java.util.Set;

/**
 * A feature which defines a ParentPom
 */
public interface ParentPomFeature extends DefaultFeature {
    @NonNull
    ParentPom getParentPom();

    @Override
    default boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return supports(applicationType) && options.getBuildTool() == BuildTool.MAVEN;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }
}
