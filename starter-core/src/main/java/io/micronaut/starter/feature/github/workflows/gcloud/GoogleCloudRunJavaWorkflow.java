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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.server.Netty;

import jakarta.inject.Singleton;

/**
 * Deploy app docker image to Google Cloud Run service.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
@Singleton
public class GoogleCloudRunJavaWorkflow extends AbstractCloudRunWorkflow {

    public static final String NAME = "github-workflow-google-cloud-run";

    public GoogleCloudRunJavaWorkflow(Netty netty) {
        super(netty, false);
    }

    @Override
    public String getTitle() {
        return "Google Cloud Run GitHub Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to deploy to Google Cloud Run from Google Container Registry";
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return "google-cloud-run.yml";
    }
}
