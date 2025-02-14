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

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.openrewrite.OpenRewriteAvailableFeatures;
import io.micronaut.starter.openrewrite.OpenRewriteFeature;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = AddFeatureCommand.NAME, description = "Modifies an existing application by adding features (dependencies, configuration, etc.)")
@Prototype
public class AddFeatureCommand extends BaseCommand implements Callable<Integer> {

    public static final String NAME = "add-feature";

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",",
            description = "The features to apply. Possible values: ${COMPLETION-CANDIDATES}",
            completionCandidates = OpenRewriteAvailableFeatures.class)
    @ReflectiveAccess
    protected List<String> features = new ArrayList<>();

    protected final AvailableFeatures availableFeatures;

    public AddFeatureCommand(OpenRewriteAvailableFeatures availableFeatures) {
        this.availableFeatures = availableFeatures;
    }

    @Override
    public Integer call() throws Exception {
        List<String> recipeNames = new ArrayList<>();
        for (String feature : features) {
            recipeNames.add(availableFeatures.findFeature(feature)
                    .filter(OpenRewriteFeature.class::isInstance)
                    .map(f -> ((OpenRewriteFeature) f).getRecipeName())
                    .orElseThrow(() -> new CommandLine.ParameterException(spec.commandLine(), "Feature [" + feature + "] is not supported by the " + NAME + " command")));
        }
        return 0;
    }

    void applyOpenRewriteRecipes(List<String> recipeNames) {
        //TODO apply OpenRewrite Recipes
    }
}
