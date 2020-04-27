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
package io.micronaut.starter.cli.command;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.Options;

import java.util.*;
import java.util.stream.Collectors;


public class ListFeatures {

    private final AvailableFeatures availableFeatures;
    private final Options options;
    private final ApplicationType command;
    private final ContextFactory contextFactory;

    public ListFeatures(AvailableFeatures availableFeatures,
                        Options options,
                        ApplicationType command,
                        ContextFactory contextFactory) {
        this.availableFeatures = availableFeatures;
        this.options = options;
        this.command = command;
        this.contextFactory = contextFactory;
    }

    void output(ConsoleOutput consoleOutput) {
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures,
                Collections.emptyList(),
                command,
                options);
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
        consoleOutput.out("  " + String.format("%1$-" + width + "s", "Name") + "Description");
        consoleOutput.out("  " + new String(new char[width - 2]).replace("\0", "-") + "  ---------------");

        featuresByCategory.forEach((category, features) -> {
            consoleOutput.out("  @|bold,underline,magenta " + category + "|@");
            listFeatures(consoleOutput, defaultFeatures, features, width);
            consoleOutput.out("");
        });
    }

    private void listFeatures(ConsoleOutput consoleOutput, Set<Feature> defaultFeatures, List<Feature> allFeatures, int width) {
        for (Feature feature: allFeatures) {
            if (defaultFeatures.contains(feature)) {
                String name = feature.getName() + " (+)";
                consoleOutput.out("@|blue   " + String.format("%1$-" + width + "s", name) + feature.getDescription() + "|@");
            } else {
                consoleOutput.out("  " + String.format("%1$-" + width + "s", feature.getName()) + feature.getDescription());
            }
        }
    }
}
