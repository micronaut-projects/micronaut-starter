package io.micronaut.starter.build

import io.micronaut.core.annotation.Nullable
import io.micronaut.starter.build.dependencies.Scope;

/**
 * You can get an instance via {@link io.micronaut.starter.build.BuildTestUtil}.
 */
interface BuildTestVerifier {

    boolean hasAnnotationProcessor(String groupId, String artifactId)

    boolean hasAnnotationProcessor(String groupId, String artifactId, boolean isTest)

    boolean hasBom(String groupId, String artifactId, Scope scope)

    boolean hasBom(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId, Scope scope)

    boolean hasDependency(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty)

    boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty)

    boolean hasDependency(String groupId, String artifactId)

    boolean hasExclusion(String groupId, String artifactId)

    boolean hasExclusion(String groupId, String artifactId, @Nullable Scope scope)

    boolean hasTestResourceDependency(String groupId, String artifactId)

    boolean hasTestResourceDependency(String artifactId)

    boolean hasDependency(String artifactId)

    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId)

    boolean hasBuildPlugin(String id)
}
