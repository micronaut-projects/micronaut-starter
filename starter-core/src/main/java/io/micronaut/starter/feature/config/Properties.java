package io.micronaut.starter.feature.config;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.PropertiesTemplate;
import io.micronaut.starter.template.YamlTemplate;

import javax.inject.Singleton;

@Singleton
public class Properties implements ConfigurationFeature {

    @Override
    public String getName() {
        return "properties";
    }

    @Override
    public String getDescription() {
        return "Creates a properties configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("propertiesConfig", new PropertiesTemplate("src/main/resources/application.properties", commandContext.getConfiguration()));
    }
}
