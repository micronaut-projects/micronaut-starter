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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.github.workflows.Secret;
import io.micronaut.starter.feature.github.workflows.WorkflowsUtils;
import io.micronaut.starter.feature.github.workflows.azure.template.azureContainerInstanceWorkflow;
import io.micronaut.starter.feature.github.workflows.azure.template.azureContainerInstanceWorkflowReadme;
import io.micronaut.starter.feature.github.workflows.docker.AbstractDockerRegistryWorkflow;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import java.util.Arrays;
import java.util.List;

/**
 * Deploy dockerized app to Azure Container Instance.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
public abstract class AbstractAzureContainerInstanceWorkflow extends AbstractDockerRegistryWorkflow {

    public static final String AZURE_CREDENTIALS = "AZURE_CREDENTIALS";
    public static final String AZURE_RESOURCE_GROUP = "AZURE_RESOURCE_GROUP";
    public static final String AZURE_DEFAULT_WORKFLOW_LOCATION = "westeurope";
    public static final String AZURE_DEFAULT_WORKFLOW_PORT = "8080";

    private final Netty netty;
    private final boolean isGraal;

    public AbstractAzureContainerInstanceWorkflow(Netty netty, boolean isGraal) {
        this.netty = netty;
        this.isGraal = isGraal;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.github.com/en/free-pro-team@latest/actions";
    }

    @Override
    public List<Secret> getSecrets() {
        return Arrays.asList(
                // Docker registry properties - Azure can use any docker registry
                new Secret(DOCKER_USERNAME, "Docker registry username. In case of Azure Container Registry, provide Azure username or Service principal ID, see more on [Azure Container Registry authentication with service principals](https://docs.microsoft.com/en-us/azure/container-registry/container-registry-auth-service-principal)."),
                new Secret(DOCKER_PASSWORD, "Docker registry password. In case of Azure Container Registry, provide Azure password or Service principal password."),
                new Secret(DOCKER_REPOSITORY_PATH, "Docker image repository. In case of Azure Container Registry, for image `micronaut.azurecr.io/foo/bar:0.1`, the `foo` is an _image repository_."),
                new Secret(DOCKER_REGISTRY_URL, "Docker registry url. In case of Azure Container Registry use the Container registry login path, e.g. for the image `micronaut.azurecr.io/foo/bar:0.1`, the `micronaut.azurecr.io` is a _registry url_."),

                // Azure credentials
                new Secret(AZURE_CREDENTIALS, "Azure Service Principal, see more on [Azure/aci-deploy#Azure Service Principal for RBAC](https://github.com/Azure/aci-deploy#azure-service-principal-for-rbac)."),
                new Secret(AZURE_RESOURCE_GROUP, "Azure Resource Group name, see more on [Resource groups](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/overview#resource-groups).")
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(ServerFeature.class)) {
            featureContext.addFeature(netty);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        String workflowFilePath = ".github/workflows/" + getWorkflowFileName(generatorContext);

        generatorContext.addTemplate("azureContainerInstanceWorkflow",
                new RockerTemplate(workflowFilePath,
                        azureContainerInstanceWorkflow.template(generatorContext.getProject(),
                                generatorContext.getBuildTool(), generatorContext.getJdkVersion(), isGraal
                ))
        );

        generatorContext.addTemplate("exampleController", WorkflowsUtils.createExampleController(
                generatorContext.getProject(), generatorContext.getLanguage()));

        generatorContext.addHelpTemplate(
                new RockerWritable(azureContainerInstanceWorkflowReadme.template(
                        this, generatorContext.getProject(), workflowFilePath)));
    }
}
