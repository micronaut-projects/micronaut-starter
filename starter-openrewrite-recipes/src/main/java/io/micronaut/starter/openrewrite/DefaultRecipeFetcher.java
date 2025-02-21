package io.micronaut.starter.openrewrite;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.sdk.BuildTool;
import io.micronaut.starter.sdk.dependency.Dependency;
import io.micronaut.starter.sdk.dependency.Scope;
import jakarta.inject.Singleton;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeException;
import org.openrewrite.config.Environment;
import org.openrewrite.maven.AddAnnotationProcessor;
import org.openrewrite.properties.AddProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import static io.micronaut.starter.openrewrite.RecipeUtils.resolveRecipe;

/**
 * <a href="https://docs.openrewrite.org/recipes/java/dependencies/adddependency">Add Gradle or Maven dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/maven/adddependency">Add Maven Dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/gradle/adddependency">Add Gradle dependency</a>
 */
@Singleton
class DefaultRecipeFetcher implements RecipeFetcher {
    private static final Dependency MICRONAUT_INJECT = Dependency.builder()
            .groupId("io.micronaut")
            .artifactId("micronaut-inject")
            .compile()
            .build();
    private final Environment env;
    private static final Function<String, Boolean> MICRONAUT_DOCUMENTATION_LINK =
            s -> (s.startsWith("https://micronaut-projects.github.io") || s.startsWith("https://docs.micronaut.io"));
    private static final Function<String, Boolean> LINK_NOT_MICRONAUT_DOC =
            s -> s.startsWith("http") && !MICRONAUT_DOCUMENTATION_LINK.apply(s);

    DefaultRecipeFetcher(Environment env) {
        this.env = env;
    }

    @Override
    @NonNull
    public List<FileContents> findAllFilesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findAllFilesContents(recipe);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
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

    private static List<FileContents> findAllFilesContents(Recipe recipe) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        List<FileContents> result = new ArrayList<>();
        if (resolvedRecipe instanceof org.openrewrite.xml.CreateXmlFile createXmlFile) {
            result.add(new FileContents(createXmlFile.getRelativeFileName(), createXmlFile.getFileContents()));
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            result.addAll(findAllFilesContents(r));
        }
        return result;
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

    @Override
    @NonNull
    public Optional<Properties> findPropertiesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findProperties(recipe);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
    }

    @Override
    public Optional<String> findMicronautDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, MICRONAUT_DOCUMENTATION_LINK);
    }

    @Override
    public Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, LINK_NOT_MICRONAUT_DOC);
    }

    private @NonNull Optional<Properties> findProperties(@NonNull Recipe recipe) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        Properties properties = new Properties();
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Recipe resolvedRecipeChild = resolveRecipe(r);
            if (resolvedRecipeChild instanceof AddProperty addProperty) {
                properties.put(addProperty.getProperty(), addProperty.getValue());
            }
            Optional<Properties> nestedPropertiesOptional = findProperties(resolvedRecipeChild);
            if (nestedPropertiesOptional.isPresent()) {
                Properties nestedProperties = nestedPropertiesOptional.get();
                nestedProperties.forEach(properties::putIfAbsent);
            }
        }
        if (properties.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(properties);
    }

    public Optional<String> findLinkInAppendToTextFileRecipeByRecipeName(String recipeName, Function<String, Boolean> contentFunction) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findLinkInAppendToTextFileRecipeByRecipeName(recipe, contentFunction);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
    }

    private Optional<String> findLinkInAppendToTextFileRecipeByRecipeName(Recipe recipe, Function<String, Boolean> contentFunction) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        if (resolvedRecipe instanceof org.openrewrite.text.AppendToTextFile appendToTextFile) {
            String content = appendToTextFile.getContent();
            if (Boolean.TRUE.equals(contentFunction.apply(content))) {
                return Optional.of(content);
            }
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Optional<String> documentation = findLinkInAppendToTextFileRecipeByRecipeName(r, contentFunction);
            if (documentation.isPresent()) {
                return documentation;
            }
        }
        return Optional.empty();
    }
}
