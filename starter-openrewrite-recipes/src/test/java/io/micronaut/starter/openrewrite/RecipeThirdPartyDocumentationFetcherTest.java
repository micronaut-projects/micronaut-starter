package io.micronaut.starter.openrewrite;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class RecipeThirdPartyDocumentationFetcherTest {
    private static final String RECIPE_MOCKITO = "io.micronaut.starter.feature.mockito";
    private static final String RECIPE_VALIDATION = "io.micronaut.starter.feature.validation";

    @Inject
    RecipeThirdPartyDocumentationFetcher fetcher;

    @Test
    void testFetchThirdPartyDocumentation() {
        Optional<String> documentation = fetcher.findThirdPartyDocumentationByRecipeName(RECIPE_VALIDATION);
        assertFalse(documentation.isPresent());

        documentation = fetcher.findThirdPartyDocumentationByRecipeName(RECIPE_MOCKITO);
        assertTrue(documentation.isPresent());
    }
}