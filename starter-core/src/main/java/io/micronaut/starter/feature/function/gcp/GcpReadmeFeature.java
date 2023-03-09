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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.ReadmeFeature;
import io.micronaut.starter.feature.function.gcp.template.gcpFunctionReadme;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class GcpReadmeFeature implements ReadmeFeature {

    @Override
    @NonNull
    public String getName() {
        return "google-cloud-function-readme";
    }

    @Override
    @NonNull
    public Optional<RockerModel> readmeTemplate(@NonNull GeneratorContext generatorContext) {
        String entryPoint = generatorContext.getFeature(EntryPointFeature.class)
                .map(f -> f.entryPoint(generatorContext))
                .orElse(null);
        return Optional.of(
                gcpFunctionReadme.template(
                        generatorContext.getProject(),
                        generatorContext.getFeatures(),
                        getRunCommand(generatorContext.getBuildTool()),
                        getBuildCommand(generatorContext.getBuildTool()),
                        generatorContext.getApplicationType() == ApplicationType.FUNCTION,
                        entryPoint
                )
        );
    }
}
