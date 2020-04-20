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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.Options;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.List;

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
        if (generatorContext.getBuildTool() == BuildTool.maven) {
            BuildProperties props = generatorContext.getBuildProperties();
            props.put("maven-surefire-plugin.version", "2.22.2");
            props.put("maven-failsafe-plugin.version", "2.22.2");
        }
        doApply(generatorContext);
    }

    void doApply(GeneratorContext generatorContext);

    TestFramework getTestFramework();

    Language getDefaultLanguage();

    default boolean isJunit() {
        return getTestFramework() == TestFramework.junit;
    }

    default boolean isSpock() {
        return getTestFramework() == TestFramework.spock;
    }

    default boolean isKotlinTest() {
        return getTestFramework() == TestFramework.kotlintest;
    }

    @Override
    default boolean shouldApply(ApplicationType applicationType,
                                Options options,
                                List<Feature> selectedFeatures) {
        return options.getTestFramework() == getTestFramework() ||
                (options.getTestFramework() == null && options.getLanguage() == getDefaultLanguage());
    }
}
