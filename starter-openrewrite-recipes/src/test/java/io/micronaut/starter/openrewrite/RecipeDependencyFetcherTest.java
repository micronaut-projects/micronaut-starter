package io.micronaut.starter.openrewrite

import io.micronaut.starter.sdk.BuildTool
import io.micronaut.starter.sdk.dependency.Dependency
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import spock.lang.Specification

@MicronautTest(startApplication = false)
class RecipeDependencyFetcherTest extends Specification {

    @Test
    void testFetchDependencies(RecipeDependencyFetcher fetcher) {
        List<Dependency> dependencies = fetcher.findAllByRecipeNameAndBuildTool("micronaut.starter.feature.mockito.AddDependencyGradleMockito", BuildTool.GRADLE)
        expect:
        dependencies.size() == 1
        dependencies[0].group == "org.mockito"
        dependencies[0].name == "mockito-core"
    }

}
