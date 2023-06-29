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
package io.micronaut.starter.springboot;

import io.micronaut.starter.build.dependencies.Dependency;

public final class SpringBootDependencies {
    public static final String GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER = "spring-boot-starter";
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER_WEB = "spring-boot-starter-web";
    private static final String ARTIFACT_ID_SPRING_BOOT_STARTER_TEST = "spring-boot-starter-test";

    public static final Dependency.Builder DEPENDENCY_SPRINGBOOT_STARTER = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER)
            .compile();

    public static final Dependency.Builder DEPENDENCY_SPRING_BOOT_STARTER_WEB = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER_WEB)
            .compile();
    public static final Dependency.Builder DEPENDENCY_SPRINGBOOT_STARTER_TEST = Dependency.builder()
            .groupId(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId(ARTIFACT_ID_SPRING_BOOT_STARTER_TEST)
            .test();

    private SpringBootDependencies() {
    }
}
