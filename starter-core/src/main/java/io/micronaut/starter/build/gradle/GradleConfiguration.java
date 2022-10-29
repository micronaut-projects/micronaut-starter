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
package io.micronaut.starter.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.Optional;

public enum GradleConfiguration implements Ordered {
    ANNOTATION_PROCESSOR("annotationProcessor", 0),
    KAPT("kapt", 1),
    API("api", 2),
    IMPLEMENTATION("implementation", 3),
    COMPILE_ONLY("compileOnly", 4),
    RUNTIME_ONLY("runtimeOnly", 5),
    NATIVE_IMAGE_COMPILE_ONLY("nativeImageCompileOnly", 6),
    TEST_ANNOTATION_PROCESSOR("testAnnotationProcessor", 7),
    TEST_KAPT("kaptTest", 8),
    TEST_IMPLEMENTATION("testImplementation", 9),
    TEST_COMPILE_ONLY("testCompileOnly", 10),
    TEST_RUNTIME_ONLY("testRuntimeOnly", 11),
    DEVELOPMENT_ONLY("developmentOnly", 12),
    OPENREWRITE("rewrite", 13),
    TEST_RESOURCES_SERVICE("testResourcesService", 14);

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

    @NonNull
    public static Optional<GradleConfiguration> of(@NonNull Scope scope,
                                                   @NonNull Language language,
                                                   @NonNull TestFramework testFramework) {
        switch (scope.getSource()) {
            case MAIN:
                if (scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                    if (language == Language.JAVA) {
                        if (testFramework == TestFramework.KOTEST) {
                            return Optional.of(GradleConfiguration.KAPT);
                        } else {
                            return Optional.of(GradleConfiguration.ANNOTATION_PROCESSOR);
                        }
                    } else if (language == Language.KOTLIN) {
                        return Optional.of(GradleConfiguration.KAPT);
                    } else if (language == Language.GROOVY) {
                        return Optional.of(GradleConfiguration.COMPILE_ONLY);
                    }
                    return Optional.of(GradleConfiguration.ANNOTATION_PROCESSOR);
                }
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(GradleConfiguration.IMPLEMENTATION);
                    }
                    return Optional.of(GradleConfiguration.RUNTIME_ONLY);
                }
                if (scope.getPhases().contains(Phase.COMPILATION)) {
                    return Optional.of(GradleConfiguration.COMPILE_ONLY);
                }
                if (scope.getPhases().contains(Phase.NATIVE_IMAGE_COMPILATION)) {
                    return Optional.of(GradleConfiguration.NATIVE_IMAGE_COMPILE_ONLY);
                }
                if (scope.getPhases().contains(Phase.OPENREWRITE)) {
                    return Optional.of(GradleConfiguration.OPENREWRITE);
                }
                if (scope.getPhases().contains(Phase.DEVELOPMENT)) {
                    return Optional.of(GradleConfiguration.DEVELOPMENT_ONLY);
                }
                if (scope.getPhases().contains(Phase.TEST_RESOURCES_SERVICE)) {
                    return Optional.of(GradleConfiguration.TEST_RESOURCES_SERVICE);
                }
                break;
            case TEST:
                if (scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                    if (language == Language.JAVA) {
                        if (testFramework == TestFramework.KOTEST) {
                            return Optional.of(GradleConfiguration.TEST_KAPT);
                        } else {
                            return Optional.of(GradleConfiguration.TEST_ANNOTATION_PROCESSOR);
                        }
                    } else if (language == Language.KOTLIN) {
                        return Optional.of(GradleConfiguration.TEST_KAPT);
                    } else if (language == Language.GROOVY) {
                        return Optional.of(GradleConfiguration.TEST_COMPILE_ONLY);
                    }
                    return Optional.of(GradleConfiguration.TEST_ANNOTATION_PROCESSOR);
                }
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION)) {
                        return Optional.of(GradleConfiguration.TEST_IMPLEMENTATION);
                    }
                    return Optional.of(GradleConfiguration.TEST_RUNTIME_ONLY);
                }
                if (scope.getPhases().contains(Phase.COMPILATION)) {
                    return Optional.of(GradleConfiguration.TEST_COMPILE_ONLY);
                }
                break;

            default:
                return Optional.empty();
        }
        return Optional.empty();
    }
}
