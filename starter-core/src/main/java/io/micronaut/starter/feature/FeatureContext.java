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

import io.micronaut.starter.Options;
import io.micronaut.starter.command.ConsoleOutput;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class FeatureContext {

    private final MicronautCommand command;
    private final List<Feature> selectedFeatures;
    private final Options options;
    private final List<Feature> features = new ArrayList<>();
    private List<FeaturePredicate> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Options options,
                          MicronautCommand command,
                          List<Feature> selectedFeatures) {
        this.command = command;
        this.selectedFeatures = selectedFeatures;
        if (options.getTestFramework() == null) {
            TestFramework testFramework = selectedFeatures.stream()
                    .filter(TestFeature.class::isInstance)
                    .map(TestFeature.class::cast)
                    .map(TestFeature::getTestFramework)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No test framework could derived from the selected features [%s]", selectedFeatures)));
            options = new Options(options.getLanguage(), testFramework, options.getBuildTool());
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

    public List<Feature> getFinalFeatures(ConsoleOutput consoleOutput) {
        return features.stream().filter(feature -> {
            for (FeaturePredicate predicate: exclusions) {
                if (predicate.test(feature)) {
                    predicate.getWarning().ifPresent(consoleOutput::warning);
                    return false;
                }
            }
            return true;
        }).collect(collectingAndThen(toList(), Collections::unmodifiableList));
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

    public Options getOptions() {
        return options;
    }

    public boolean hasApplicationFeature() {
        return features.stream().anyMatch(feature -> feature instanceof ApplicationFeature);
    }

    public void addFeature(Feature feature) {
        if (iterator != null) {
            iterator.add(feature);
        } else {
            features.add(feature);
        }
        feature.processSelectedFeatures(this);
    }

    public MicronautCommand getCommand() {
        return command;
    }

    public boolean isPresent(Class<? extends Feature> feature) {
        return features.stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }
}
