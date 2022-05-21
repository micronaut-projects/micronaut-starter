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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.github.workflows.GitHubWorkflowFeature;
import io.micronaut.starter.feature.github.workflows.Secret;
import io.micronaut.starter.options.BuildTool;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract docker registry workflow.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
public abstract class AbstractDockerRegistryWorkflow extends GitHubWorkflowFeature {

    public static final String DOCKER_USERNAME = "DOCKER_USERNAME";
    public static final String DOCKER_PASSWORD = "DOCKER_PASSWORD";
    public static final String DOCKER_REPOSITORY_PATH = "DOCKER_REPOSITORY_PATH";
    public static final String DOCKER_REGISTRY_URL = "DOCKER_REGISTRY_URL";

    @Override
    public List<Secret> getSecrets() {
        return Arrays.asList(
                new Secret(DOCKER_USERNAME, "Username for Docker registry authentication."),
                new Secret(DOCKER_PASSWORD, "Docker registry password."),
                new Secret(DOCKER_REPOSITORY_PATH, "Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`."),
                new Secret(DOCKER_REGISTRY_URL, "Docker registry url.")
        );
    }

    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().equals(BuildTool.MAVEN)) {
            generatorContext.getBuildProperties().put("jib.docker.image", "${project.artifactId}");
            generatorContext.getBuildProperties().put("jib.docker.tag", "${project.version}");
        }
    }

    public abstract String getWorkflowFileName(GeneratorContext generatorContext);
}
