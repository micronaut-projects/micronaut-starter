package io.micronaut.starter.openrewrite;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RecipePropertiesFetcherTest {
    private static final String NAME = "io.micronaut.starter.feature.liquibase";
    @Test
    void testFetchProperties(RecipeFetcher fetcher) {
        Optional<Properties> propertiesOptional = fetcher.findPropertiesByRecipeName(NAME);
        assertTrue(propertiesOptional.isPresent());
        Properties properties = propertiesOptional.get();
        assertEquals("classpath:db/liquibase-changelog.xml", properties.get("liquibase.datasources.default.change-log"));
    }
}