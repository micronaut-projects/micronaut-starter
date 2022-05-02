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
package io.micronaut.starter.feature.agora.gru;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Singleton
public class GruHttpFeatureValidator  implements FeatureValidator {
    private static final List<TestFramework> SUPPORTED_TEST_FRAMEWORKS = Arrays.asList(TestFramework.JUNIT, TestFramework.SPOCK);

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(GruHttp.class::isInstance) && !SUPPORTED_TEST_FRAMEWORKS.contains(options.getTestFramework())) {
            throw new IllegalArgumentException("gru-http requires either Spock or Junit 5");
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        /*
         * validation only required in preprocessing
         */
    }
}
