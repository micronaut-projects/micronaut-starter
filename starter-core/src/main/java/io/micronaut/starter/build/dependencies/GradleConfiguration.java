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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.order.Ordered;

public enum GradleConfiguration implements Ordered {
    ANNOTATION_PROCESSOR("annotationProcessor", 0),
    API("api", 1),
    IMPLEMENTATION("implementation", 2),
    COMPILE_ONLY("compileOnly", 3),
    COMPILE_ONLY_API("compileOnlyApi", 4),
    RUNTIME_ONLY("runtimeOnly", 5),
    TEST_ANNOTATION_PROCESSOR("annotationProcessor", 6),
    TEST_IMPLEMENTATION("testImplementation", 7),
    TEST_COMPILE_ONLY("testCompileOnly", 8),
    TEST_RUNTIME_ONLY("testRuntimeOnly", 9);

    private final String configurationName;
    private final int order;

    GradleConfiguration(String configurationName, int order) {
        this.configurationName = configurationName;
        this.order = order;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    @Override
    public String toString() {
        return this.configurationName;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
