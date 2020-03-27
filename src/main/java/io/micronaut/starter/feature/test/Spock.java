package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.TestFeature;
import io.micronaut.starter.template.BinaryTemplate;

public class Spock implements TestFeature {

    @Override
    public String getName() {
        return "spock";
    }

    @Override
    public void doApply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("testDir", new BinaryTemplate("src/test/groovy/{packageName}/.gitkeep", classLoader.getResource(".gitkeep")));
    }
}
