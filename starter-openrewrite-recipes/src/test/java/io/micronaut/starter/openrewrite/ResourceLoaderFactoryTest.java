package io.micronaut.starter.openrewrite;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.openrewrite.config.Environment;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(startApplication = false)
class ResourceLoaderFactoryTest {

    private static final String NAME = "micronaut.starter.feature.mockito.AddDependencyMockito";

    @Test
    void loadRecipes(Environment env) {
        assertFalse(env.listRecipes().isEmpty());
        var recipe = env.activateRecipes(NAME);
        assertFalse(recipe.getRecipeList().isEmpty());
    }
}
