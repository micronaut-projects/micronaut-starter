package io.micronaut.starter.openrewrite;

import io.micronaut.context.exceptions.ConfigurationException;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeException;
import org.openrewrite.config.Environment;

import java.util.Optional;
import java.util.function.Function;

import static io.micronaut.starter.openrewrite.RecipeUtils.resolveRecipe;

public abstract class DocumentationFetcher {
    private final Environment env;
    private final Function<String, Boolean> contentFunction;
    public DocumentationFetcher(Environment env,
                                Function<String, Boolean> contentFunction) {
        this.env = env;
        this.contentFunction = contentFunction;
    }

    public Optional<String> findLinkByRecipeName(String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findLinkByRecipeName(recipe);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
    }

    private Optional<String> findLinkByRecipeName(Recipe recipe) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        if (resolvedRecipe instanceof org.openrewrite.text.AppendToTextFile appendToTextFile) {
            String content = appendToTextFile.getContent();
            if (Boolean.TRUE.equals(contentFunction.apply(content))) {
                return Optional.of(content);
            }
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Optional<String> documentation = findLinkByRecipeName(r);
            if (documentation.isPresent()) {
                return documentation;
            }
        }
        return Optional.empty();
    }
}
