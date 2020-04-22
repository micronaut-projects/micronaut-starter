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
package io.micronaut.starter.feature.other;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.azure.AzureFunction;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.List;

/**
 * Adds a shaded JAR feature.
 */
@Singleton
public class ShadePlugin implements DefaultFeature {

    @Override
    public String getTitle() {
        return "Shaded JAR";
    }

    @Override
    public String getDescription() {
        return "Adds Support for Producing a Shaded (FAT) JAR";
    }

    @NonNull
    @Override
    public String getName() {
        return "shade";
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        return selectedFeatures.stream()
                .noneMatch(f -> f.getName().equals("jib") || f.getName().equals(AzureFunction.NAME));
    }
}
