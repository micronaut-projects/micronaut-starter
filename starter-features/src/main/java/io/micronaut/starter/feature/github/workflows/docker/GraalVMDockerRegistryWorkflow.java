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
package io.micronaut.starter.feature.github.workflows.docker;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.github.workflows.docker.templates.dockerRegistryWorkflow;
import io.micronaut.starter.feature.github.workflows.docker.templates.dockerRegistryWorkflowReadme;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

/**
 * Push GraalVM native image to docker registry github workflow feature.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
@Singleton
public class GraalVMDockerRegistryWorkflow extends AbstractDockerRegistryWorkflow {

    public static final String NAME = "github-workflow-graal-docker-registry";

    @Override
    public String getTitle() {
        return "Push GraalVM Native Image To Docker Registry Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to build and push a GraalVM native image to a Docker registry";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.github.com/en/free-pro-team@latest/actions";
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        String workflowFilePath = ".github/workflows/" + getWorkflowFileName(generatorContext);

        generatorContext.addTemplate("graalCEWorkflow",
                new RockerTemplate(workflowFilePath,
                        dockerRegistryWorkflow.template(generatorContext.getProject(), generatorContext.getJdkVersion(),
                                generatorContext.getBuildTool(), true)
                )
        );

        generatorContext.addHelpTemplate(new RockerWritable(dockerRegistryWorkflowReadme.template(
                this, generatorContext.getProject(), workflowFilePath)));
    }

    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return "graalvm.yml";
    }
}
