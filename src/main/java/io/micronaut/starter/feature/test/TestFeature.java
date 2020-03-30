package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.BuildTool;

import java.util.Map;

public interface TestFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }

    @Override
    default void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            Map<String, String> props = commandContext.getProjectProperties();
            props.put("maven-surefire-plugin.version", "2.22.2");
            props.put("maven-failsafe-plugin.version", "2.22.2");
        }
        doApply(commandContext);
    }

    void doApply(CommandContext commandContext);

    default boolean isJunit() {
        return false;
    }

    default boolean isSpock() {
        return false;
    }

    default boolean isKotlinTest() {
        return false;
    }
}
