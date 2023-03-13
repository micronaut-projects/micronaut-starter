package io.micronaut.starter.build

import io.micronaut.starter.build.dependencies.Scope;

/**
 * You can get an instance via {@link io.micronaut.starter.build.BuildTestUtil}.
 */
interface BuildTestVerifier {

    boolean hasAnnotationProcessor(String groupId, String artifactId)

    boolean hasBom(String groupId, String artifactId, Scope scope)

    boolean hasBom(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId, Scope scope)

    boolean hasDependency(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId)

    boolean hasTestResourceDependency(String groupId, String artifactId)

    boolean hasTestResourceDependency(String artifactId)

    boolean hasDependency(String artifactId)

    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId)
}
