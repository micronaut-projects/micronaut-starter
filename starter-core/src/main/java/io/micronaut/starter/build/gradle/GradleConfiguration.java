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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.build.KotlinSymbolProcessing;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.Optional;

public enum GradleConfiguration implements Ordered {
    ANNOTATION_PROCESSOR("annotationProcessor", 0),
    KAPT("kapt", 1),
    KSP("ksp", 2),
    API("api", 3),
    IMPLEMENTATION("implementation", 4),
    COMPILE_ONLY("compileOnly", 5),
    RUNTIME_ONLY("runtimeOnly", 6),
    NATIVE_IMAGE_COMPILE_ONLY("nativeImageCompileOnly", 7),
    TEST_ANNOTATION_PROCESSOR("testAnnotationProcessor", 8),
    TEST_KAPT("kaptTest", 9),
    TEST_KSP("kspTest", 10),
    TEST_IMPLEMENTATION("testImplementation", 11),
    TEST_COMPILE_ONLY("testCompileOnly", 12),
    TEST_RUNTIME_ONLY("testRuntimeOnly", 13),
    DEVELOPMENT_ONLY("developmentOnly", 14),
    OPENREWRITE("rewrite", 15),
    TEST_RESOURCES_SERVICE("testResourcesService", 16);

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
                                                   @NonNull TestFramework testFramework,
                                                   @Nullable GeneratorContext generatorContext) {
        switch (scope.getSource()) {
            case MAIN:
                if (scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                    if (language == Language.JAVA) {
                        if (testFramework == TestFramework.KOTEST) {
                            return Optional.of(kotlinAnnotationProcessor(generatorContext));
                        } else {
                            return Optional.of(GradleConfiguration.ANNOTATION_PROCESSOR);
                        }
                    } else if (language == Language.KOTLIN) {
                        return Optional.of(kotlinAnnotationProcessor(generatorContext));
                    } else if (language == Language.GROOVY) {
                        return Optional.of(GradleConfiguration.COMPILE_ONLY);
                    }
                    return Optional.of(GradleConfiguration.ANNOTATION_PROCESSOR);
                }
                if (scope.getPhases().contains(Phase.RUNTIME)) {
                    if (scope.getPhases().contains(Phase.COMPILATION) && scope.getPhases().contains(Phase.PUBLIC_API)) {
                        return Optional.of(GradleConfiguration.API);

                    } else if (scope.getPhases().contains(Phase.COMPILATION)) {
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
                            return Optional.of(kotlinTestAnnotationProcessor(generatorContext));
                        } else {
                            return Optional.of(GradleConfiguration.TEST_ANNOTATION_PROCESSOR);
                        }
                    } else if (language == Language.KOTLIN) {
                        return Optional.of(kotlinTestAnnotationProcessor(generatorContext));
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

    private static GradleConfiguration kotlinAnnotationProcessor(GeneratorContext generatorContext) {
        if (generatorContext != null && generatorContext.isFeaturePresent(KotlinSymbolProcessing.class)) {
            return GradleConfiguration.KSP;
        }
        return GradleConfiguration.KAPT;
    }

    private static GradleConfiguration kotlinTestAnnotationProcessor(GeneratorContext generatorContext) {
        if (generatorContext != null && generatorContext.isFeaturePresent(KotlinSymbolProcessing.class)) {
            return GradleConfiguration.TEST_KSP;
        }
        return GradleConfiguration.TEST_KAPT;
    }
}
