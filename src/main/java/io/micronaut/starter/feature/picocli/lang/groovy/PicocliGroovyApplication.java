package io.micronaut.starter.feature.picocli.lang.groovy;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.GroovyApplicationFeature;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class PicocliGroovyApplication implements GroovyApplicationFeature {

    @Override
    public String mainClassName(Project project) {
        return project.getPackageName() + "." + project.getClassName() + "Command";
    }

    @Override
    public String getName() {
        return "application";
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_CLI;
    }

    @Override
    public void apply(CommandContext commandContext) {
        GroovyApplicationFeature.super.apply(commandContext);

        commandContext.addTemplate("application", getTemplate(commandContext.getProject()));
    }

    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate(getPath(),
                picocliApplication.template(project));
    }

    protected String getPath() {
        return "src/main/groovy/{packagePath}/{className}Command.groovy";
    }
}
