package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class GroovyApplication implements ApplicationFeature {

    @Override
    public String mainClassName(Project project) {
        return project.getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "application";
    }

    @Override
    public boolean supports(String command) {
        return command.equals(CreateAppCommand.NAME);
    }

    @Override
    public void apply(CommandContext commandContext) {
        ApplicationFeature.super.apply(commandContext);

        commandContext.addTemplate("application", new RockerTemplate("src/main/groovy/{packagePath}/Application.groovy",
                application.template(commandContext.getProject(), commandContext.getFeatures())));
    }

    protected String getPath() {
        return "src/main/groovy/{packagePath}/Application.groovy";
    }
}
