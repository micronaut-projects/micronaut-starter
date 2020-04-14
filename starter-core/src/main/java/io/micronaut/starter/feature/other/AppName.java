package io.micronaut.starter.feature.other;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.externalconfig.ExternalConfigFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class AppName implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, TestFramework testFramework, BuildTool buildTool, List<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public String getName() {
        return "app-name";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(CommandContext commandContext) {
        Map<String, Object> appNameConfig;
        if (commandContext.isFeaturePresent(ExternalConfigFeature.class)) {
            appNameConfig = commandContext.getBootstrapConfig();
        } else {
            appNameConfig = commandContext.getConfiguration();
        }
        appNameConfig.put("micronaut.application.name", commandContext.getProject().getName());
    }
}
