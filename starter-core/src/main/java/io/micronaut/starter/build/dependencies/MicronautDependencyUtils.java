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
    public static final String GROUP_ID_MICRONAUT_CRAC = "io.micronaut.crac";
    public static final String GROUP_ID_MICRONAUT_SERDE = "io.micronaut.serde";
    public static final String GROUP_ID_MICRONAUT_SECURITY = "io.micronaut.security";
    public static final String GROUP_ID_MICRONAUT_TRACING = "io.micronaut.tracing";
    public static final String GROUP_ID_MICRONAUT_TEST = "io.micronaut.test";
    public static final String GROUP_ID_MICRONAUT_R2DBC = "io.micronaut.r2dbc";
    public static final String GROUP_ID_MICRONAUT_DATA = "io.micronaut.data";
    public static final String GROUP_ID_MICRONAUT_SQL = "io.micronaut.sql";

    public static final String GROUP_ID_MICRONAUT_KOTLIN = "io.micronaut.kotlin";

    public static final String GROUP_ID_MICRONAUT_GROOVY = "io.micronaut.groovy";

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

    @NonNull
    public static Dependency.Builder testDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_TEST);
    }

    @NonNull
    public static Dependency.Builder r2dbcDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_R2DBC);
    }

    @NonNull
    public static Dependency.Builder tracingDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_TRACING);
    }

    @NonNull
    public static Dependency.Builder dataDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_DATA);
    }

    @NonNull
    public static Dependency.Builder sqlDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_SQL);
    }

    @NonNull
    public static Dependency.Builder kotlinDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_KOTLIN);
    }

    @NonNull
    public static Dependency.Builder groovyDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_GROOVY);
    }

    @NonNull
    private static Dependency.Builder micronautDependency(@NonNull String groupId) {
        return Dependency.builder()
                .groupId(groupId);
    }

    public static Dependency.Builder cracDependency() {
        return micronautDependency(GROUP_ID_MICRONAUT_CRAC);
    }
}
