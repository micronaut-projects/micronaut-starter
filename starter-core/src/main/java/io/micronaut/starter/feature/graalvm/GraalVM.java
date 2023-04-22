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
package io.micronaut.starter.feature.graalvm;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class GraalVM implements Feature {
    public static final String FEATURE_NAME_GRAALVM = "graalvm";
    public static final String ARTIFACT_ID_MICRONAUT_GRAALVM = "micronaut-graal";

    static final Dependency GRAAL_SVM = Dependency.builder()
            .groupId("org.graalvm.nativeimage")
            .artifactId("svm")
            .compileOnly()
            .build();

    @Override
    public String getName() {
        return FEATURE_NAME_GRAALVM;
    }

    @Override
    public String getTitle() {
        return "GraalVM Native Image";
    }

    @Override
    public String getDescription() {
        return "Allows Building a GraalVM Native Image";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addDependency(GRAAL_SVM);
        }
    }

    public static Dependency.Builder micronautGraalVM() {
        return MicronautDependencyUtils.coreDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_GRAALVM);
    }
}
