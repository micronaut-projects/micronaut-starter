package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.options.TestFramework;
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
        commandContext.addTemplate("testDir", new URLTemplate("src/test/" + commandContext.getLanguage().name() + "/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.junit;
    }
}
