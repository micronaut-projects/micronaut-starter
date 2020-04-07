package io.micronaut.starter.feature.picocli.test.kotlintest;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class PicocliKotlinTest implements Feature {

    @Override
    public String getName() {
        return "picocli-kotlintest";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("picocliKotlinTest", getTemplate(commandContext.getProject()));
    }

    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate("src/test/groovy/{packagePath}/{className}CommandSpec.kt", picocliKotlinTestTest.template(project));
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_CLI;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}