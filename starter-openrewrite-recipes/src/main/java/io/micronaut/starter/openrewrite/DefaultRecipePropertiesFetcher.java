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
        Properties properties = new Properties();
        for (Recipe r : recipe.getRecipeList()) {
            if (r instanceof AddProperty addProperty) {
                properties.put(addProperty.getProperty(), addProperty.getValue());
            }
        }
        if (properties.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(properties);
    }
}
