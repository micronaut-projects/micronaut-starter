package io.micronaut.starter.feature;

import io.micronaut.starter.command.CommandContext;

public class Junit implements TestFeature {

    @Override
    public String getName() {
        return "junit";
    }

    @Override
    public void apply(CommandContext commandContext) {

    }
}
