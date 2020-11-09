/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.github.workflows.docker;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.github.workflows.docker.templates.*;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import javax.inject.Singleton;

/**
 * Push to docker registry github workflow feature.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
@Singleton
public class DockerRegistryWorkflow extends AbstractDockerRegistryWorkflow {

    public static final String NAME = "github-workflow-docker-registry";

    @Override
    public String getTitle() {
        return "Push To Docker Registry Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds GitHub workflow that builds and pushes docker image to any docker registry.";
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
        if (generatorContext.getBuildTool().equals(BuildTool.MAVEN)) {
            generatorContext.getBuildProperties().put("jib.docker.image", "${project.name}");
            generatorContext.getBuildProperties().put("jib.docker.tag", "${project.version}");
            generatorContext.addTemplate("mavenJavaWorkflow",
                    new RockerTemplate(".github/workflows/maven.yml",
                            mavenJavaWorkflow.template(generatorContext.getProject(), generatorContext.getJdkVersion())
                    )
            );
        } else {
            generatorContext.addTemplate("gradleJavaWorkflow",
                    new RockerTemplate(".github/workflows/gradle.yml",
                            gradleJavaWorkflow.template(generatorContext.getJdkVersion(), generatorContext.getProject())
                    )
            );
        }

        generatorContext.addHelpTemplate(new RockerWritable(dockerRegistryWorkflowReadme.template(this, generatorContext.getProject())));
    }
}
