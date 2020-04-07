package io.micronaut.starter.feature;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.options.BuildTool;

import java.util.Set;

public interface ApplicationFeature extends Feature {

    String mainClassName(Project project);

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            commandContext.getProjectProperties().put("exec.mainClass", mainClassName(commandContext.getProject()));
        }
    }
}
