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
package io.micronaut.starter.feature.validation;

import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.MinJdkFeature;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class MinJdkFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {
        JdkVersion jdk = options.javaVersion();
        for (Feature f : features) {
            if (f instanceof MinJdkFeature feature) {
                JdkVersion min = feature.minJdk();
                if (!jdk.greaterThanEqual(min)) {
                    throw new IllegalArgumentException("The selected feature %s requires at latest Java %d".formatted(f.getName(), min.majorVersion()));
                }
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {

    }
}
