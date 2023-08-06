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
package io.micronaut.starter.feature;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class FeatureContext {

    private final ApplicationType applicationType;
    private final OperatingSystem operatingSystem;
    private final Set<Feature> selectedFeatures;
    private final Options options;
    private final List<Feature> features = new ArrayList<>();
    private final List<FeaturePredicate> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Options options,
                          ApplicationType applicationType,
                          @Nullable OperatingSystem operatingSystem,
                          Set<Feature> selectedFeatures) {
        this.applicationType = applicationType;
        this.operatingSystem = operatingSystem;
        this.selectedFeatures = selectedFeatures;
        if (options.getTestFramework() == null) {
            TestFramework testFramework = selectedFeatures.stream()
                    .filter(TestFeature.class::isInstance)
                    .map(TestFeature.class::cast)
                    .map(TestFeature::getTestFramework)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No test framework could derived from the selected features [%s]".formatted(selectedFeatures)));
            options = options.withTestFramework(testFramework);
        }
        this.options = options;
    }

    public void processSelectedFeatures() {
        features.addAll(0, selectedFeatures);
        features.sort(Comparator.comparingInt(Feature::getOrder));
        iterator = features.listIterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            feature.processSelectedFeatures(this);
        }
        iterator = null;
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

    public Language getLanguage() {
        return options.getLanguage();
    }

    public String getFramework() {
        return options.getFramework();
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

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public Set<Feature> getSelectedFeatures() {
        return Collections.unmodifiableSet(selectedFeatures);
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

    public boolean isPresent(Class<? extends Feature> feature) {
        return features.stream()
                .filter(f -> exclusions.stream().noneMatch(e -> e.test(f)))
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }

    public Optional<Feature> getFeature(Class<? extends Feature> feature) {
        return features.stream()
                .filter(f -> exclusions.stream().noneMatch(e -> e.test(f)))
                .filter(f -> feature.isAssignableFrom(f.getClass()))
                .findFirst();
    }

    public void addFeatureIfNotPresent(Class<? extends Feature> featureClass, Feature feature) {
        if (!isPresent(featureClass)) {
            addFeature(feature);
        }
    }
}
