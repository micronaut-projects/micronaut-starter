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
import io.micronaut.starter.cli.openrewrite.OpenRewriteRecipesRunner;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.openrewrite.OpenRewriteAvailableFeatures;
import io.micronaut.starter.openrewrite.OpenRewriteConfiguration;
import io.micronaut.starter.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.sdk.BuildTool;
import jakarta.inject.Named;
import org.apache.maven.shared.invoker.*;
import picocli.CommandLine;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = AddFeatureCommand.NAME, description = "Modifies an existing application by adding features (dependencies, configuration, etc.)")
@Prototype
public class AddFeatureCommand extends BaseCommand implements Callable<Integer> {

    public static final String NAME = "add-feature";

    @CommandLine.Option(
            names = {"-f", "--features"},
            paramLabel = "FEATURE",
            split = ",",
            description = "The features to apply. Possible values: ${COMPLETION-CANDIDATES}",
            completionCandidates = OpenRewriteAvailableFeatures.class,
            required = true)
    @ReflectiveAccess
    protected List<String> features = new ArrayList<>();

    @CommandLine.Option(
            names = {"--folder"},
            description = "project folder",
            required = true)
    @ReflectiveAccess
    protected File folder;

    protected final OpenRewriteRecipesRunner mavenRecipeRunner;
    protected final OpenRewriteRecipesRunner gradleRecipeRunner;
    protected final AvailableFeatures availableFeatures;

    public AddFeatureCommand(OpenRewriteAvailableFeatures availableFeatures,
                             @Named("maven") OpenRewriteRecipesRunner mavenRecipeRunner,
                             @Named("gradle") OpenRewriteRecipesRunner gradleRecipeRunner) {
        this.availableFeatures = availableFeatures;
        this.mavenRecipeRunner = mavenRecipeRunner;
        this.gradleRecipeRunner = gradleRecipeRunner;
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
        applyOpenRewriteRecipes(recipeNames, folder);
        return 0;
    }

    void applyOpenRewriteRecipes(List<String> recipes, File folder) {
        BuildTool buildTool = BuildTool.GRADLE;
        File pomXml = new File(folder, "pom.xml");
        if (pomXml.exists()) {
            buildTool = BuildTool.MAVEN;
        }
        OpenRewriteConfiguration configuration = OpenRewriteConfiguration.builder()
                .activeRecipes(recipes)
                .exportDatatables(true)
                .recipeChangeLogLevel("INFO")
                .configLocation("/Users/sdelamo/github/micronaut-projects/micronaut-starter/starter-openrewrite-recipes/src/main/resources/META-INF/rewrite/rewrite.yml")
                .operatingSystem(getOperatingSystem())
                .build();

        if (buildTool == BuildTool.MAVEN) {
            mavenRecipeRunner.run(recipes, folder, configuration, this::out, this::err);
        } else if (buildTool.isGradle()) {
            gradleRecipeRunner.run(recipes, folder, configuration, this::out, this::err);
        }
    }

}


