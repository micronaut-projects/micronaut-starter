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
package io.micronaut.starter.feature.function.azure;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.ReadmeFeature;
import io.micronaut.starter.feature.function.azure.template.azureFunctionReadme;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class AzureReadmeFeature implements ReadmeFeature {
    private static final String NAME = "azure-function-readme";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public Optional<RockerModel> readmeTemplate(GeneratorContext generatorContext) {
        return Optional.of(
                azureFunctionReadme.template(
                        generatorContext.getProject(),
                        generatorContext.getFeatures(),
                        getRunCommand(generatorContext.getBuildTool()),
                        getBuildCommand(generatorContext.getBuildTool()),
                        generatorContext.getBuildTool())
        );
    }

    @Override
    @NonNull
    public String getRunCommand(@NonNull BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package azure-functions:run";
        } else {
            return "gradlew clean azureFunctionsRun";
        }
    }

    @Override
    @NonNull
    public String getBuildCommand(@NonNull BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package azure-functions:deploy";
        } else if (buildTool.isGradle()) {
            return "gradlew clean azureFunctionsDeploy";
        } else {
            throw new IllegalStateException("Unsupported build tool");
        }
    }
}
