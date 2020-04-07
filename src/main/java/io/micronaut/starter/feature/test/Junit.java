package io.micronaut.starter.feature.test;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.java.picocliJunitTest;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;

@Singleton
public class Junit implements TestFeature {

    @Override
    public String getName() {
        return "junit";
    }

    @Override
    public void doApply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("testDir", new URLTemplate("src/test/java/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
        if (commandContext.getCommand() == MicronautCommand.CREATE_CLI) {
            commandContext.addTemplate("picocliTest", getPicocliTemplate(commandContext.getProject()));
        }
    }

    public RockerTemplate getPicocliTemplate(Project project) {
        return new RockerTemplate("src/test/java/{packagePath}/{className}CommandTest.java", picocliJunitTest.template(project));
    }

    @Override
    public boolean isJunit() {
        return true;
    }
}
