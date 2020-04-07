package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.groovy.picocliSpockTest;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;

@Singleton
public class Spock implements TestFeature {

    @Override
    public String getName() {
        return "spock";
    }

    @Override
    public void doApply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("testDir", new URLTemplate("src/test/groovy/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
        if (commandContext.getCommand() == MicronautCommand.CREATE_CLI) {
            commandContext.addTemplate("picocliTest", new RockerTemplate("src/test/groovy/{packagePath}/{className}CommandSpec.groovy", picocliSpockTest.template(commandContext.getProject())));
        }

    }

    @Override
    public boolean isSpock() {
        return true;
    }
}
