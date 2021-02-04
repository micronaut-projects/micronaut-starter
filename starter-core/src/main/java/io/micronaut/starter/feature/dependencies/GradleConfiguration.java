/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.dependencies;

public enum GradleConfiguration {
    API("api"),
    IMPLEMENTATION("implementation"),
    COMPILE_ONLY("compileOnly"),
    COMPILE_ONLY_API("compileOnlyApi"),
    RUNTIME_ONLY("runtimeOnly"),
    TEST_IMPLEMENTATION("testImplementation"),
    TEST_COMPILE_ONLY("testCompileOnly"),
    TEST_RUNTIME_ONLY("testRuntimeOnly");

    private final String configurationName;

    GradleConfiguration(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getConfigurationName() {
        return configurationName;
    }
}
