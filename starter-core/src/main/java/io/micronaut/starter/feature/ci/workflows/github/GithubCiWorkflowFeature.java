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
package io.micronaut.starter.feature.ci.workflows.github;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.starter.feature.ci.workflows.github.templates.javaAction;
import io.micronaut.starter.options.JdkDistribution;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Singleton
public class GithubCiWorkflowFeature extends CIWorkflowFeature {
    public static final String NAME = "github-workflow-ci";

    private static final String WORKFLOW_BASE_PATH = ".github/workflows/";
    private static final String DEFAULT_BRANCH = "main";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Github Actions CI Workflow";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addTemplate("javaAction", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        String workflowFilePath = WORKFLOW_BASE_PATH + getWorkflowFileName(generatorContext);
        return new RockerTemplate(workflowFilePath, javaAction.template(
            generatorContext.getJdkVersion(),
            JdkDistribution.DEFAULT_DISTRIBUTION,
            generatorContext.getBuildTool(),
            DEFAULT_BRANCH)
        );
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.github.com/en/actions";
    }

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        switch (generatorContext.getBuildTool()) {
            case GRADLE:
            case GRADLE_KOTLIN:
                return "gradle.yml";
            case MAVEN:
                return "maven.yml";
            default:
                throw new IllegalArgumentException("Unexpected constant for BuildTool enum");
        }
    }
}
