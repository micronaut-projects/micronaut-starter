package io.micronaut.starter.feature.ci.workflows.oci;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.starter.feature.ci.workflows.oci.templates.buildSpec;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Singleton
public class OCICiWorkflowFeature extends CIWorkflowFeature {

    public static final String NAME = "oci-devops-build-ci";
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
        return new RockerTemplate(getWorkflowFileName(generatorContext), buildSpec.template(
                generatorContext.getProject().getName(),
                generatorContext.getJdkVersion(),
                generatorContext.getBuildTool()
        ));
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/en-us/iaas/Content/devops/using/build_specs.htm";
    }
}