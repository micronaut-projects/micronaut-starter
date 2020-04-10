package io.micronaut.starter.feature.reloading;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;

@Singleton
public class Jrebel implements ReloadingFeature {

    @Override
    public String getName() {
        return "jrebel";
    }

    @Override
    public String getDescription() {
        return "Adds support for class reloading with JRebel (requires separate JRebel installation)";
    }

    @Override
    public void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.gradle) {
            commandContext.getBuildProperties().addComment("TODO: Replace with agent path from JRebel installation; see documentation");
            commandContext.getBuildProperties().addComment("rebelAgent=-agentpath:~/bin/jrebel/lib/jrebel6/lib/libjrebel64.dylib");
        }
    }
}
