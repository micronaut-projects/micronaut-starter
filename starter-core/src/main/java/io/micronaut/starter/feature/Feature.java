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
package io.micronaut.starter.feature;

import io.micronaut.core.naming.Described;
import io.micronaut.core.naming.Named;
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.Language;

import java.util.Optional;

public interface Feature extends Named, Ordered, Described {

    String getTitle();

    default int getOrder() {
        return FeaturePhase.DEFAULT.getOrder();
    }

    default void processSelectedFeatures(FeatureContext featureContext) {

    }

    default void apply(GeneratorContext generatorContext) {

    }

    default boolean supports(ApplicationType command) {
        return true;
    }

    default boolean isVisible() {
        return true;
    }

    default Optional<Language> getRequiredLanguage() {
        return Optional.empty();
    }

}
