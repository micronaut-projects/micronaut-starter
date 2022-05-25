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
package io.micronaut.starter.feature.github.workflows.azure;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.server.Netty;

import jakarta.inject.Singleton;

/**
 * Deploy GraalVM native image to Azure Container Instance.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
@Singleton
public class AzureContainerInstanceGraalWorkflow extends AbstractAzureContainerInstanceWorkflow {

    public static final String NAME = "github-workflow-azure-container-instance-graalvm";

    public AzureContainerInstanceGraalWorkflow(Netty netty) {
        super(netty, true);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Azure Container Instance GraalVM Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to deploy a GraalVM native image to Azure Container Instance";
    }

    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return "azure-container-instance-graalvm.yml";
    }
}
