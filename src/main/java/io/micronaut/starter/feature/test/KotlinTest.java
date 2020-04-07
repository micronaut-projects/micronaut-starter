package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.groovy.picocliSpockTest;
import io.micronaut.starter.feature.picocli.kotlin.picocliKotlinTest;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;

@Singleton
public class KotlinTest implements TestFeature {

    @Override
    public String getName() {
        return "kotlinTest";
    }

    @Override
    public void doApply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("kotlinTestConfig",
                new URLTemplate("src/test/kotlin/io/kotlintest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotlintest/ProjectConfig.kt")));
        commandContext.addTemplate("testDir", new URLTemplate("src/test/kotlin/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
        if (commandContext.getCommand() == MicronautCommand.CREATE_CLI) {
            commandContext.addTemplate("picocliTest", new RockerTemplate("src/test/kotlin/{packagePath}/{className}CommandSpec.kt", picocliKotlinTest.template(commandContext.getProject())));
        }
    }

    @Override
    public boolean isKotlinTest() {
        return true;
    }
}
