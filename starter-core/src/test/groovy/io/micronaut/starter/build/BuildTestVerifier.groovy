/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build

import io.micronaut.starter.build.dependencies.Scope;

/**
 * You can get an instance via {@link io.micronaut.starter.build.BuildTestUtil}.
 */
interface BuildTestVerifier {

    boolean hasBom(String groupId, String artifactId, Scope scope)

    boolean hasBom(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId, Scope scope)

    boolean hasDependency(String groupId, String artifactId, String scope)

    boolean hasDependency(String groupId, String artifactId)

    boolean hasTestResourceDependency(String groupId, String artifactId)

    boolean hasTestResourceDependency(String artifactId)

    boolean hasDependency(String artifactId)

    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId)

    boolean hasBuildPlugin(String id)
}
