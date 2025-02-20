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
import org.openrewrite.maven.AddAnnotationProcessor;

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
    private static final Dependency MICRONAUT_INJECT = Dependency.builder()
            .groupId("io.micronaut")
            .artifactId("micronaut-inject")
            .compile()
            .build();
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
        Recipe resolvedRecipe = RecipeUtils.resolveRecipe(recipe);
        if (resolvedRecipe instanceof org.openrewrite.java.dependencies.AddDependency d) {
            dependencies.add(findDependency(d));
        } else if (buildTool.isGradle() && resolvedRecipe instanceof org.openrewrite.gradle.AddDependency d) {
            dependencies.add(findGradleDependency(d));
        } else if (buildTool == BuildTool.MAVEN && resolvedRecipe instanceof org.openrewrite.maven.AddDependency d) {
            dependencies.add(findMavenDependency(d));
        } else if (buildTool == BuildTool.MAVEN && resolvedRecipe instanceof org.openrewrite.maven.AddAnnotationProcessor d) {
            dependencies.add(findMavenAnnotationProcessor(d));
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Recipe resolvedRecipeChild = RecipeUtils.resolveRecipe(r);
            dependencies.addAll(findDependencies(resolvedRecipeChild, buildTool));
        }
        return dependencies;
    }

    @NonNull
    private static Dependency findMavenAnnotationProcessor(@NonNull AddAnnotationProcessor recipe) {
        return Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId())
                .exclude(MICRONAUT_INJECT)
                .versionProperty(recipe.getVersion())
                .annotationProcessor(false)
                .build();
    }

    private static Dependency findDependency(org.openrewrite.java.dependencies.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String scope = recipe.getScope();
        if (scope != null) {
            ofMavenScope(scope).ifPresent(builder::scope);
        }
        String configuration = recipe.getConfiguration();
        if (configuration != null) {
            ofGradleConfiguration(configuration).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private static Dependency findGradleDependency(org.openrewrite.gradle.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String configuration = recipe.getConfiguration();
        if (configuration != null) {
            ofGradleConfiguration(configuration).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private static Dependency findMavenDependency(org.openrewrite.maven.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String scope = recipe.getScope();
        if (scope != null) {
            ofMavenScope(scope).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private static Optional<Scope> ofGradleConfiguration(String configuration) {
        if (configuration.equals("implementation")) {
            return Optional.of(Scope.COMPILE);
        } else if (configuration.equals("compileOnly")) {
            return Optional.of(Scope.COMPILE_ONLY);
        } else if (configuration.equals("annotationProcessor")) {
            return Optional.of(Scope.ANNOTATION_PROCESSOR);
        } else if (configuration.equals("testAnnotationProcessor")) {
            return Optional.of(Scope.TEST_ANNOTATION_PROCESSOR);
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
