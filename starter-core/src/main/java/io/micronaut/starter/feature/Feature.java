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

/**
 * A feature is a class that affects the outcome of a generated
 * project.
 *
 * @author James Kleeh
 * @since 2.0.0
 */
public interface Feature extends Named, Ordered, Described {

    /**
     * @return The title of the feature
     */
    default String getTitle() {
        return null;
    }

    @Override
    default String getDescription() {
        return null;
    }

    /**
     * The order of a feature controls the order in which it applies. The
     * order of a feature is important to ensure that any previous work done
     * by other features that may be required is done. See {@link FeaturePhase}
     * for a list of phases to choose from.
     *
     * @return The order of the feature
     */
    default int getOrder() {
        return FeaturePhase.DEFAULT.getOrder();
    }

    /**
     * If this method is called for a given feature then that feature was explicitly selected
     * or was included by default as a result of {@link DefaultFeature#shouldApply(ApplicationType, io.micronaut.starter.options.Options, java.util.Set)}.
     *
     * This method can be implemented to allow features to modify the list of features
     * that will apply to the project. The methods {@link FeatureContext#addFeature(Feature)}
     * and {@link FeatureContext#exclude(FeaturePredicate)} are the primary ways to add and
     * remove features from the context. {@link FeatureContext#isPresent(Class)} can be used
     * to determine the existence of other features in order to make decisions.
     *
     * @param featureContext The feature context
     */
    default void processSelectedFeatures(FeatureContext featureContext) {

    }

    /**
     * If this method is called for a given feature that means the feature was explicitly selected,
     * included by default as a result of {@link DefaultFeature#shouldApply(ApplicationType, io.micronaut.starter.options.Options, java.util.Set)},
     * or added explicitly by another feature through {@link FeatureContext#addFeature(Feature)}.
     *
     * At this point the feature list is set and cannot change.
     *
     * This method can be implemented to modify the generated project. The feature can add templates
     * by executing {@link GeneratorContext#addTemplate(String, io.micronaut.starter.template.Template)}, modify configuration
     * by modifying {@link GeneratorContext#getConfiguration()} or {@link GeneratorContext#getBootstrapConfig()}, or modify build properties through {@link GeneratorContext#getBuildProperties()}.
     *
     * @param generatorContext THe generator context
     */
    default void apply(GeneratorContext generatorContext) {

    }

    /**
     * This method must be implemented to ensure it is only selectable for the desired
     * application types. This method is not used for determining if a default feature
     * should be applied.
     *
     * @param applicationType The application type
     * @return True if the feature can be selected by the user
     */
    boolean supports(ApplicationType applicationType);

    /**
     * Some features should not be visible to the user because they are a common parent of other
     * selectable features, or they should always be applied, or any other reason.
     *
     * @return True if the feature should able to be selected by the user
     */
    default boolean isVisible() {
        return true;
    }

    /**
     * @return The language this feature requires, or empty if the feature can be used with
     * any language.
     */
    default Optional<Language> getRequiredLanguage() {
        return Optional.empty();
    }

    /**
     * @return The {@link Category} to which the feature belongs to.
     */
    default String getCategory() {
        return Category.OTHER;
    }

}
