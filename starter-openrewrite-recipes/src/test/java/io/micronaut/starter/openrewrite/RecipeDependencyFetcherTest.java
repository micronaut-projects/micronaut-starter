package io.micronaut.starter.openrewrite;

import io.micronaut.starter.sdk.BuildTool;
import io.micronaut.starter.sdk.dependency.Dependency;
import io.micronaut.starter.sdk.dependency.Scope;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import spock.lang.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class RecipeDependencyFetcherTest extends Specification {
    private static final String NAME = "micronaut.starter.feature.mockito.AddDependencyMockito";

    @Test
    void testFetchDependencies(RecipeDependencyFetcher fetcher) {
        List<Dependency> dependencies = fetcher.findAllByRecipeNameAndBuildTool(NAME, BuildTool.GRADLE);
        assertEquals(1, dependencies.size());
        Dependency dependency = dependencies.get(0);
        assertMockitoDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(NAME, BuildTool.GRADLE_KOTLIN);
        assertEquals(1, dependencies.size());
        dependency = dependencies.get(0);
        assertMockitoDependency(dependency);

        dependencies = fetcher.findAllByRecipeNameAndBuildTool(NAME, BuildTool.MAVEN);
        assertEquals(1, dependencies.size());
        dependency = dependencies.get(0);
        assertMockitoDependency(dependency);
    }

    void assertMockitoDependency(Dependency dependency) {
        assertEquals("org.mockito", dependency.getGroupId());
        assertEquals("mockito-core", dependency.getArtifactId());
        assertEquals(Scope.TEST, dependency.getScope());
    }
}
