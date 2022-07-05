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
package io.micronaut.starter.feature.ci.workflows.gcp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.starter.feature.ci.workflows.gcp.templates.cloudBuild;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Singleton
public class GoogleCloudCiWorkflowFeature extends CIWorkflowFeature {
    public static final String NAME = "google-cloud-workflow-ci";
    private static final String WORKFLOW_FILENAME = "cloudbuild.yaml";

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return WORKFLOW_FILENAME;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Cloud Build CI Workflow";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds a Google Cloud Build configuration file to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addTemplate("cloudBuild", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        return new RockerTemplate(WORKFLOW_FILENAME, cloudBuild.template(
                generatorContext.getProject().getName(),
                generatorContext.getJdkVersion(),
                generatorContext.getBuildTool())
        );
    }

    @NonNull
    @Override
    public String getThirdPartyDocumentation() {
        return "https://cloud.google.com/build/docs/building/build-java";
    }
}
