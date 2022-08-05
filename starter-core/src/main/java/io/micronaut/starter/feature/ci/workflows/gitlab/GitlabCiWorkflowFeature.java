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
package io.micronaut.starter.feature.ci.workflows.gitlab;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.starter.feature.ci.workflows.gitlab.templates.gitlabciGradle;
import io.micronaut.starter.feature.ci.workflows.gitlab.templates.gitlabciMaven;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Singleton
public class GitlabCiWorkflowFeature extends CIWorkflowFeature {
    public static final String NAME = "gitlab-workflow-ci";

    private static final String WORKFLOW_FILE_NAME = ".gitlab-ci.yml";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Gitlab-CI Workflow";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a Gitlab CI Workflow to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addTemplate("gitlabci", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        switch (generatorContext.getBuildTool()) {
            case GRADLE:
            case GRADLE_KOTLIN:
                return new RockerTemplate(WORKFLOW_FILE_NAME, gitlabciGradle.template()
                );
            case MAVEN:
                return new RockerTemplate(WORKFLOW_FILE_NAME, gitlabciMaven.template(
                        generatorContext.getJdkVersion())
                );
            default:
                throw new IllegalArgumentException("Unexpected constant for BuildTool enum");
        }

    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gitlab.com/ee/ci/";
    }

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return WORKFLOW_FILE_NAME;
    }
}
