package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
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
    }

    @Override
    public boolean isSpock() {
        return true;
    }
}
