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
package io.micronaut.starter.feature.test;

import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import jakarta.inject.Singleton;

import java.util.Set;

/**
 *
 * Validates features of type {@link JunitCompanionFeature} require JUnit as Testing framework.
 * @author Sergio del Amo
 * @since 3.7.0
 */
@Singleton
public class JunitCompanionFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options,
                                      Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options,
                                       Set<Feature> features) {
        if (options.testFramework() != TestFramework.JUNIT) {
            features.stream()
                    .filter(JunitCompanionFeature.class::isInstance)
                    .findFirst()
                    .ifPresent(f -> {
                        throw new IllegalArgumentException(f.getName() + " requires JUnit.");
                    });
        }
    }
}
