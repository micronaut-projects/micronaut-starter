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
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteConfiguration;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteRecipesRunner;
import jakarta.inject.Named;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;


@Command(name = "update", description = "Update an existing project to latest Micronaut version")
@Prototype
public class UpdateCommand extends BaseCommand implements Callable<Integer> {

    @ReflectiveAccess
    @Option(
        names = {"--project", "-p"},
        required = false,
        paramLabel = "DIR",
        description = "The project folder (defaults to current directory)"
    )
    protected File projectDir;

    private final OpenRewriteRecipesRunner gradleRecipeRunner;
    private final OpenRewriteRecipesRunner mavenRecipeRunner;

    public UpdateCommand(@Named("gradle") OpenRewriteRecipesRunner gradleRecipeRunner,
                         @Named("maven") OpenRewriteRecipesRunner mavenRecipeRunner) {
        this.gradleRecipeRunner = gradleRecipeRunner;
        this.mavenRecipeRunner = mavenRecipeRunner;
    }

    @Override
    public Integer call() {
        try {
            if (projectDir == null || !projectDir.exists() || !projectDir.isDirectory()) {
                err("Invalid project directory: " + (projectDir == null ? "null" : projectDir.getAbsolutePath()));
                return 2;
            }

            List<String> recipes = List.of("io.micronaut.openrewrite.update");
            OpenRewriteConfiguration configuration = OpenRewriteConfiguration.builder()
                    .activeRecipes(recipes)
                    .exportDatatables(true)
                    .recipeChangeLogLevel("INFO")
                    .operatingSystem(getOperatingSystem())
                    .build();

            OpenRewriteRecipesRunner runner = selectRunner(projectDir);
            if (runner == null) {
                err("Could not detect build tool in " + projectDir.getAbsolutePath() + ". Expected one of: pom.xml, build.gradle, build.gradle.kts");
                return 3;
            }

            runner.run(recipes, projectDir, configuration, this::out, this::err);
            return 0;
        } catch (Exception e) {
            if (showStacktrace()) {
                e.printStackTrace(outWriter().orElseGet(() -> new java.io.PrintWriter(System.out)));
            }
            err(e.getMessage() != null ? e.getMessage() : e.toString());
            return 1;
        }
    }

    /**
     * Select the appropriate OpenRewrite runner by detecting the build tool in the project directory.
     * - Maven if pom.xml exists
     * - Gradle if build.gradle or build.gradle.kts exists
     */
    private OpenRewriteRecipesRunner selectRunner(File dir) {
        File pom = new File(dir, "pom.xml");
        if (pom.isFile()) {
            return mavenRecipeRunner;
        }
        File gradleGroovy = new File(dir, "build.gradle");
        File gradleKts = new File(dir, "build.gradle.kts");
        if (gradleGroovy.isFile() || gradleKts.isFile()) {
            return gradleRecipeRunner;
        }
        File settingsGroovy = new File(dir, "settings.gradle");
        File settingsKts = new File(dir, "settings.gradle.kts");
        if (settingsGroovy.isFile() || settingsKts.isFile()) {
            return gradleRecipeRunner;
        }
        return null;
    }
}
