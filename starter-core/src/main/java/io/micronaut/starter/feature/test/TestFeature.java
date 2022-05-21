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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;

import java.util.Set;

public interface TestFeature extends DefaultFeature {

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        doApply(generatorContext);
    }

    void doApply(GeneratorContext generatorContext);

    TestFramework getTestFramework();

    default boolean isJunit() {
        return getTestFramework() == TestFramework.JUNIT;
    }

    default boolean isSpock() {
        return getTestFramework() == TestFramework.SPOCK;
    }

    default boolean isKotlinTestFramework() {
        return isKoTest();
    }

    default boolean isKoTest() {
        return getTestFramework() == TestFramework.KOTEST;
    }

    @Override
    default boolean shouldApply(ApplicationType applicationType,
                                Options options,
                                Set<Feature> selectedFeatures) {
        TestFramework selectedTest = options.getTestFramework();
        if (selectedTest == null) {
            selectedTest = options.getLanguage().getDefaults().getTest();
        }
        return supports(applicationType) && selectedTest == getTestFramework();
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }
}
