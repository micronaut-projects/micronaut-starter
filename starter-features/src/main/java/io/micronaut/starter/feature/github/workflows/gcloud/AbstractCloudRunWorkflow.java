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
package io.micronaut.starter.feature.github.workflows.gcloud;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.github.workflows.Secret;
import io.micronaut.starter.feature.github.workflows.WorkflowsUtils;
import io.micronaut.starter.feature.github.workflows.docker.AbstractDockerRegistryWorkflow;
import io.micronaut.starter.feature.github.workflows.gcloud.templates.gcloudCloudRunWorkflow;
import io.micronaut.starter.feature.github.workflows.gcloud.templates.gcloudCloudRunWorkflowReadme;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pavol Gressa
 * @since 2.3
 */
public abstract class AbstractCloudRunWorkflow extends AbstractDockerRegistryWorkflow {

    public static final String GCLOUD_PROJECT_ID = "GCLOUD_PROJECT_ID";
    public static final String GCLOUD_SA_KEY = "GCLOUD_SA_KEY";
    public static final String GCLOUD_IMAGE_REPOSITORY = "GCLOUD_IMAGE_REPOSITORY";
    public static final String GCLOUD_DEFAULT_REGION = "europe-west3";
    public static final String GCLOUD_DEFAULT_GCR = "eu.gcr.io";

    private final Netty netty;
    private final boolean isGraal;

    public AbstractCloudRunWorkflow(Netty netty, boolean isGraal) {
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
                new Secret(GCLOUD_PROJECT_ID, "Project id."),
                new Secret(GCLOUD_SA_KEY, "Service account key file. See more on [Creating and managing service accounts](https://cloud.google.com/iam/docs/creating-managing-service-accounts#iam-service-accounts-create-gcloud) and [Deployment permissions for CloudRun](https://cloud.google.com/run/docs/reference/iam/roles#additional-configuration)"),
                new Secret(GCLOUD_IMAGE_REPOSITORY, "(Optional) Docker image repository in GCR. For image `[GCLOUD_REGION]/[GCLOUD_PROJECT_ID]/foo/bar:0.1`, the `foo` is an _image repository_.")
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

        generatorContext.addTemplate("gcloudCloudRunWorkflow",
                new RockerTemplate(workflowFilePath,
                        gcloudCloudRunWorkflow.template(generatorContext.getProject(), generatorContext.getBuildTool(),
                                generatorContext.getJdkVersion(), isGraal)
                )
        );

        generatorContext.addTemplate("exampleController", WorkflowsUtils.createExampleController(
                generatorContext.getProject(), generatorContext.getLanguage()));

        generatorContext.addHelpTemplate(new RockerWritable(gcloudCloudRunWorkflowReadme.template(
                this, generatorContext.getProject(), workflowFilePath)));
    }
}
