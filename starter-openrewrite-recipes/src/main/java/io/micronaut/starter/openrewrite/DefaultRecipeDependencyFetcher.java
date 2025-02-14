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
package io.micronaut.starter.openrewrite;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.sdk.BuildTool;
import io.micronaut.starter.sdk.dependency.Dependency;
import jakarta.inject.Singleton;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeException;
import org.openrewrite.config.Environment;
import io.micronaut.starter.sdk.dependency.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <a href="https://docs.openrewrite.org/recipes/java/dependencies/adddependency">Add Gradle or Maven dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/maven/adddependency">Add Maven Dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/gradle/adddependency">Add Gradle dependency</a>
 */
@Singleton
public class DefaultRecipeDependencyFetcher implements RecipeDependencyFetcher {
    private final Environment env;

    public DefaultRecipeDependencyFetcher(Environment env) {
        this.env = env;
    }

    @Override
    @NonNull
    public List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipeName, @NonNull BuildTool buildTool) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findDependencies(recipe, buildTool);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
    }

    private static List<Dependency> findDependencies(Recipe recipe, BuildTool buildTool) {
        List<Dependency> dependencies = new ArrayList<>();
        for (Recipe r : recipe.getRecipeList()) {
            findDependency(r).ifPresent(dependencies::add);
            if (buildTool.isGradle()) {
                findGradleDependency(r).ifPresent(dependencies::add);
            } else if (buildTool == BuildTool.MAVEN) {
                findMavenDependency(r).ifPresent(dependencies::add);
            }
        }
        return dependencies;
    }
    private static Optional<Dependency> findDependency(Recipe recipe) {
        if (recipe instanceof org.openrewrite.java.dependencies.AddDependency addDependency) {
            Dependency.Builder builder = Dependency.builder()
                    .groupId(addDependency.getGroupId())
                    .artifactId(addDependency.getArtifactId());
            if (StringUtils.isNotEmpty(addDependency.getVersion())) {
                builder.version(addDependency.getVersion());
            }
            String scope = addDependency.getScope();
            if (scope != null) {
                ofMavenScope(scope).ifPresent(builder::scope);
            }
            String configuration = addDependency.getConfiguration();
            if (configuration != null) {
                ofGradleConfiguration(configuration).ifPresent(builder::scope);
            }

            return Optional.of(builder.build());
        }
        return Optional.empty();
    }
    private static Optional<Dependency> findGradleDependency(Recipe recipe) {
        if (recipe instanceof org.openrewrite.gradle.AddDependency addDependency) {
            Dependency.Builder builder = Dependency.builder()
                    .groupId(addDependency.getGroupId())
                    .artifactId(addDependency.getArtifactId());
            if (StringUtils.isNotEmpty(addDependency.getVersion())) {
                builder.version(addDependency.getVersion());
            }
            String configuration = addDependency.getConfiguration();
            if (configuration != null) {
                ofGradleConfiguration(configuration).ifPresent(builder::scope);
            }
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }

    private static Optional<Dependency> findMavenDependency(Recipe recipe) {
        if (recipe instanceof org.openrewrite.maven.AddDependency addDependency) {
            Dependency.Builder builder = Dependency.builder()
                    .groupId(addDependency.getGroupId())
                    .artifactId(addDependency.getArtifactId());
            if (StringUtils.isNotEmpty(addDependency.getVersion())) {
                builder.version(addDependency.getVersion());
            }
            String scope = addDependency.getScope();
            if (scope != null) {
                ofMavenScope(scope).ifPresent(builder::scope);
            }
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }

    private static Optional<Scope> ofGradleConfiguration(String configuration) {
        if (configuration.equals("implementation")) {
            return Optional.of(Scope.COMPILE);
        } else if (configuration.equals("compileOnly")) {
            return Optional.of(Scope.COMPILE_ONLY);
        } else if (configuration.equals("runtimeOnly")) {
            return Optional.of(Scope.RUNTIME);
        } else if (configuration.equals("testRuntimeOnly")) {
            return Optional.of(Scope.TEST_RUNTIME);
        } else if (configuration.equals("testImplementation")) {
            return Optional.of(Scope.TEST);
        }
        //TODO other configurations
        return Optional.empty();
    }

    private static Optional<Scope> ofMavenScope(String scope) {
        if (scope.equals("compile")) {
            return Optional.of(Scope.COMPILE);
        } else if (scope.equals("runtime")) {
            return Optional.of(Scope.RUNTIME);
        } else if (scope.equals("test")) {
            return Optional.of(Scope.TEST);
        }
        //TODO other scopes
        return Optional.empty();
    }
}
