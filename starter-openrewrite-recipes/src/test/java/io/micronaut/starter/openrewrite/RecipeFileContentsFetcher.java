package io.micronaut.starter.openrewrite;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RecipeFileContentsFetcher {
    private static final String RECIPE_LIQUIBASE = "io.micronaut.starter.feature.liquibase";

    @Test
    void testFetchMicronautDocumentation(RecipeFetcher fetcher) {
        List<FileContents> files = fetcher.findAllFilesByRecipeName(RECIPE_LIQUIBASE);
        assertFalse(files.isEmpty());
        assertTrue(files.stream().anyMatch(f -> f.relativeFileName().equals("src/main/resources/db/liquibase-changelog.xml")));
        assertTrue(files.stream().anyMatch(f -> f.relativeFileName().equals("src/main/resources/db/changelog/01-schema.xml")));

        assertNotNull(files.stream().filter(f -> f.relativeFileName().equals("src/main/resources/db/liquibase-changelog.xml")).findFirst().orElseThrow().fileContents());
        assertNotNull(files.stream().filter(f -> f.relativeFileName().equals("src/main/resources/db/changelog/01-schema.xml")).findFirst().orElseThrow().fileContents());
    }
}
