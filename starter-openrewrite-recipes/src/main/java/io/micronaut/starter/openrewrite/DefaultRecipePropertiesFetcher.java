package io.micronaut.starter.openrewrite;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeException;
import org.openrewrite.config.Environment;

import java.util.Optional;
import java.util.Properties;

import org.openrewrite.properties.AddProperty;
import static io.micronaut.starter.openrewrite.RecipeUtils.*;

@Singleton
public class DefaultRecipePropertiesFetcher implements RecipePropertiesFetcher {
    private final Environment env;

    public DefaultRecipePropertiesFetcher(Environment env) {
        this.env = env;
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
}
