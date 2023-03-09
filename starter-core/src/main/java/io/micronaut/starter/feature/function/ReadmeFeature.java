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
package io.micronaut.starter.feature.function;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;

import java.util.Optional;

public interface ReadmeFeature extends Feature {
    @NonNull
    Optional<RockerModel> readmeTemplate(@NonNull GeneratorContext generatorContext);

    default String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw mn:run";
        } else {
            return "gradlew run";
        }
    }

    default String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else if (buildTool.isGradle()) {
            return "gradlew clean assemble";
        } else {
            throw new IllegalStateException("Unsupported build tool");
        }
    }

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        addReadmeTemplate(generatorContext);
    }

    default void addReadmeTemplate(@NonNull GeneratorContext generatorContext) {
        readmeTemplate(generatorContext)
                .ifPresent(rockerModel -> generatorContext.addHelpTemplate(new RockerWritable(rockerModel)));
    }
}
