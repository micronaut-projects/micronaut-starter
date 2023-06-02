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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.feature.graalvm.GraalVM;

public interface AwsMicronautRuntimeFeature extends MicronautRuntimeFeature {

    @Override
    @NonNull
    default String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return "lambda";
        }
        return generatorContext.getFeatures().contains(GraalVM.FEATURE_NAME_GRAALVM) ? "lambda_provided" : "lambda_java";
    }
}
