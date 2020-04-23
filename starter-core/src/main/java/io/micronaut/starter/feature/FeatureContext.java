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

import io.micronaut.starter.options.*;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.test.TestFeature;

import java.util.*;

import static java.util.stream.Collectors.*;

public class FeatureContext {

    private final ApplicationType command;
    private final Set<Feature> selectedFeatures;
    private final Options options;
    private final List<Feature> features = new ArrayList<>();
    private final List<FeaturePredicate> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Options options,
                          ApplicationType command,
                          Set<Feature> selectedFeatures) {
        this.command = command;
        this.selectedFeatures = selectedFeatures;
        if (options.getTestFramework() == null) {
            TestFramework testFramework = selectedFeatures.stream()
                    .filter(TestFeature.class::isInstance)
                    .map(TestFeature.class::cast)
                    .map(TestFeature::getTestFramework)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No test framework could derived from the selected features [%s]", selectedFeatures)));
            options = options.withTestFramework(testFramework);
        }
        this.options = options;
    }

    public void processSelectedFeatures() {
        this.features.addAll(0, selectedFeatures);
        this.features.sort(Comparator.comparingInt(Feature::getOrder));
        this.iterator = this.features.listIterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            feature.processSelectedFeatures(this);
        }
        this.iterator = null;
    }

    public void exclude(FeaturePredicate exclusion) {
        exclusions.add(exclusion);
    }

    public Set<Feature> getFinalFeatures(ConsoleOutput consoleOutput) {
        return features.stream().filter(feature -> {
            for (FeaturePredicate predicate: exclusions) {
                if (predicate.test(feature)) {
                    predicate.getWarning().ifPresent(consoleOutput::warning);
                    return false;
                }
            }
            return true;
        }).collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Language getLanguage() {
        return options.getLanguage();
    }

    public TestFramework getTestFramework() {
        return options.getTestFramework();
    }

    public BuildTool getBuildTool() {
        return options.getBuildTool();
    }

    public JdkVersion getJavaVersion() {
        return options.getJavaVersion();
    }

    public Options getOptions() {
        return options;
    }

    public boolean hasApplicationFeature() {
        return features.stream().anyMatch(feature -> feature instanceof ApplicationFeature);
    }

    /**
     * Adds a feature to be applied. The added feature is processed immediately.
     *
     * @param feature The feature to add
     */
    public void addFeature(Feature feature) {
        if (iterator != null) {
            iterator.add(feature);
        } else {
            features.add(feature);
        }
        feature.processSelectedFeatures(this);
    }

    public ApplicationType getApplicationType() {
        return command;
    }

    public boolean isPresent(Class<? extends Feature> feature) {
        return features.stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }
}
