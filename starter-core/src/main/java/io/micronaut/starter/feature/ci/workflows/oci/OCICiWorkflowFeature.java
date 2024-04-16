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
package io.micronaut.starter.feature.ci.workflows.oci;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.starter.feature.ci.workflows.oci.templates.buildSpec;
import io.micronaut.starter.feature.ci.workflows.oci.templates.buildSpecGraal;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

@Singleton
public class OCICiWorkflowFeature extends CIWorkflowFeature {

    public static final String NAME = "oracle-cloud-devops-build-ci";
    private static final String WORKFLOW_FILENAME = "build_spec.yml";

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return WORKFLOW_FILENAME;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud DevOps Build CI Workflow";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a Oracle Cloud DevOps Build Workflow to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addTemplate("buildSpec", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        if (!generatorContext.getFeatures().hasFeature(GraalVM.class)) {
            return new RockerTemplate(getWorkflowFileName(generatorContext), buildSpec.template(
                    generatorContext.getProject().getName(),
                    generatorContext.getJdkVersion(),
                    generatorContext.getBuildTool()
            ));
        } else {
            return new RockerTemplate(getWorkflowFileName(generatorContext), buildSpecGraal.template(
                    generatorContext.getProject().getName(),
                    generatorContext.getJdkVersion(),
                    generatorContext.getBuildTool(),
                    VersionInfo.getDependencyVersion("graal").getValue()
            ));
        }
    }

    @Override
    public boolean isPreview() {
        return false;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/en-us/iaas/Content/devops/using/build_specs.htm";
    }
}
