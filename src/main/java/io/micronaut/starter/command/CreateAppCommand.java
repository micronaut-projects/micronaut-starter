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
package io.micronaut.starter.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.feature.validation.FeatureValidator;
import picocli.CommandLine;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = CreateAppCommand.NAME, description = "Creates an application")
@Prototype
public class CreateAppCommand extends CreateCommand {

    public static final String NAME = "create-app";

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",", description = "The features to use. Possible values: ${COMPLETION-CANDIDATES}", completionCandidates = CreateAppFeatures.class)
    List<String> features = new ArrayList<>();

    public CreateAppCommand(CreateAppFeatures createAppFeatures,
                            FeatureValidator featureValidator,
                            ContextFactory contextFactory) {
        super(createAppFeatures, featureValidator, contextFactory, MicronautCommand.CREATE_APP);
    }

    @Override
    protected List<String> getSelectedFeatures() {
        return features;
    }

    @Singleton
    public static class CreateAppFeatures extends AvailableFeatures {

        public CreateAppFeatures(List<Feature> features) {
            super(features.stream()
                    .filter(f -> f.supports(MicronautCommand.CREATE_APP))
                    .collect(Collectors.toList()));
        }
    }
}
