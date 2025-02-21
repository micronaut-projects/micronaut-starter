package io.micronaut.starter.openrewrite;

import io.micronaut.starter.sdk.BuildTool;
import io.micronaut.starter.sdk.dependency.Dependency;
import io.micronaut.starter.sdk.dependency.Scope;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class RecipeDependencyFetcherTest {
    private static final String RECIPE_MOCKITO = "io.micronaut.starter.feature.mockito";
    private static final String RECIPE_LIQUIBASE = "io.micronaut.starter.feature.liquibase";
    private static final String RECIPE_VALIDATION = "io.micronaut.starter.feature.validation";

    @Test
    void testFetchDependencies(RecipeFetcher fetcher) {
        List<Dependency> dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_MOCKITO, BuildTool.GRADLE);
        assertEquals(1, dependencies.size());
        Dependency dependency = dependencies.get(0);
        assertMockitoDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_MOCKITO, BuildTool.GRADLE_KOTLIN);
        assertEquals(1, dependencies.size());
        dependency = dependencies.get(0);
        assertMockitoDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_MOCKITO, BuildTool.MAVEN);
        assertEquals(1, dependencies.size());
        dependency = dependencies.get(0);
        assertMockitoDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_LIQUIBASE, BuildTool.GRADLE);
        assertEquals(1, dependencies.size());
        dependency = dependencies.get(0);
        assertLiquibaseDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_VALIDATION, BuildTool.GRADLE);
        assertEquals(3, dependencies.size());
        assertTrue(dependencies.stream().anyMatch(d -> "jakarta.validation".equals(d.getGroupId()) && "jakarta.validation-api".equals(d.getArtifactId()) && d.getScope() == Scope.COMPILE));
        assertTrue(dependencies.stream().anyMatch(d -> "io.micronaut.validation".equals(d.getGroupId()) && "micronaut-validation".equals(d.getArtifactId()) && d.getScope() == Scope.COMPILE));
        assertTrue(dependencies.stream().anyMatch(d -> "io.micronaut.validation".equals(d.getGroupId()) && "micronaut-validation-processor".equals(d.getArtifactId()) && d.getScope() == Scope.ANNOTATION_PROCESSOR));

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(RECIPE_VALIDATION, BuildTool.MAVEN);
        assertEquals(3, dependencies.size());
        assertTrue(dependencies.stream().anyMatch(d -> "jakarta.validation".equals(d.getGroupId()) && "jakarta.validation-api".equals(d.getArtifactId()) && d.getScope() == Scope.COMPILE));
        assertTrue(dependencies.stream().anyMatch(d -> "io.micronaut.validation".equals(d.getGroupId()) && "micronaut-validation".equals(d.getArtifactId()) && d.getScope() == Scope.COMPILE));
        assertTrue(dependencies.stream().anyMatch(d -> "io.micronaut.validation".equals(d.getGroupId()) && "micronaut-validation-processor".equals(d.getArtifactId()) && d.getScope() == Scope.ANNOTATION_PROCESSOR));
    }

    private static void assertMockitoDependency(Dependency dependency) {
        assertEquals("org.mockito", dependency.getGroupId());
        assertEquals("mockito-core", dependency.getArtifactId());
        assertEquals(Scope.TEST, dependency.getScope());
    }

    private static void assertLiquibaseDependency(Dependency dependency) {
        assertEquals("io.micronaut.liquibase", dependency.getGroupId());
        assertEquals("micronaut-liquibase", dependency.getArtifactId());
        assertEquals(Scope.COMPILE, dependency.getScope());
    }
}
