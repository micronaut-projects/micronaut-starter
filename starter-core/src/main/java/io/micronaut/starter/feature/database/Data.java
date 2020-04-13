package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;

@Singleton
public class Data implements Feature {

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            commandContext.getBuildProperties().put("micronaut.data.version", "1.0.2");
        }
    }
}
