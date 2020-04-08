package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.Picocli;
import io.micronaut.starter.options.TestFramework;
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
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.kotlintest;
    }
}
