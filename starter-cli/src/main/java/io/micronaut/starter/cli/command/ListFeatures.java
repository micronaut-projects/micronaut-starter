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
package io.micronaut.starter.cli.command;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.Options;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ListFeatures {

    private final AvailableFeatures availableFeatures;
    private final Options options;
    private final ApplicationType applicationType;
    private final OperatingSystem operatingSystem;
    private final ContextFactory contextFactory;

    public ListFeatures(AvailableFeatures availableFeatures,
                        Options options,
                        ApplicationType applicationType,
                        @Nullable OperatingSystem operatingSystem,
                        ContextFactory contextFactory) {
        this.availableFeatures = availableFeatures;
        this.options = options;
        this.applicationType = applicationType;
        this.operatingSystem = operatingSystem;
        this.contextFactory = contextFactory;
    }

    void output(ConsoleOutput consoleOutput) {
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures,
                Collections.emptyList(),
                applicationType,
                options,
                operatingSystem
                );
        GeneratorContext generatorContext = contextFactory.createGeneratorContext(null, featureContext, ConsoleOutput.NOOP);

        Set<Feature> defaultFeatures = generatorContext.getFeatures().getFeatures();
        List<Feature> allFeatures = availableFeatures.getFeatures().collect(Collectors.toList());

        int width = allFeatures.stream()
                .map(Feature::getName)
                .max(Comparator.comparingInt(String::length))
                .map(String::length).get() + 8;

        Map<String, List<Feature>> featuresByCategory = allFeatures.stream()
                .sorted(Comparator.comparing(Feature::getName))
                .collect(Collectors.groupingBy(Feature::getCategory));
        featuresByCategory = new TreeMap<>(featuresByCategory);

        consoleOutput.out("Available Features");
        consoleOutput.out("@|blue (+)|@ denotes the feature is included by default");
        consoleOutput.out("  " + ("%1$-" + width + "s").formatted("Name") + "Description");
        consoleOutput.out("  " + new String(new char[width - 2]).replace("\0", "-") + "  ---------------");

        featuresByCategory.forEach((category, features) -> {
            consoleOutput.out("  @|bold,underline,magenta " + category + "|@");
            listFeatures(consoleOutput, defaultFeatures, features, width);
            consoleOutput.out("");
        });
    }

    private void listFeatures(ConsoleOutput consoleOutput, Set<Feature> defaultFeatures, List<Feature> allFeatures, int width) {
        for (Feature feature: allFeatures) {
            String name = feature.getName();
            if (feature.isPreview()) {
                name += " [PREVIEW]";
            }
            if (feature.isCommunity()) {
                name += " [COMMUNITY]";
            }
            if (defaultFeatures.contains(feature)) {
                name += " (+)";
                consoleOutput.out("@|blue   " + ("%1$-" + width + "s").formatted(name) + feature.getDescription() + "|@");
            } else {
                consoleOutput.out("  " + ("%1$-" + width + "s").formatted(name) + feature.getDescription());
            }
        }
    }
}
