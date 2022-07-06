/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.annotation.NonNull;

public final class MicronautDependencyUtils {
    public static final String GROUP_ID_MICRONAUT = "io.micronaut";
    public static final String GROUP_ID_MICRONAUT_AWS = "io.micronaut.aws";
    public static final String GROUP_ID_MICRONAUT_SERDE = "io.micronaut.serde";
    public static final String GROUP_ID_MICRONAUT_SECURITY = "io.micronaut.security";
    public static final String GROUP_ID_MICRONAUT_TEST = "io.micronaut.test";

    @NonNull
    public static Dependency.Builder coreDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT);
    }

    @NonNull
    public static Dependency.Builder awsDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_AWS);
    }

    @NonNull
    public static Dependency.Builder serdeDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SERDE);
    }

    @NonNull
    public static Dependency.Builder securityDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SECURITY);
    }

    public static Dependency.Builder testDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_TEST);
    }

    @NonNull
    private static Dependency.Builder micronautDependency(@NonNull String groupId) {
        return Dependency.builder()
                .groupId(groupId);
    }
}
