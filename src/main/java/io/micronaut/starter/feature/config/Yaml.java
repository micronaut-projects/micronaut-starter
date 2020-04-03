package io.micronaut.starter.feature.config;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.YamlTemplate;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Yaml implements ConfigurationFeature, DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(f -> f instanceof ConfigurationFeature);
    }

    @Override
    public String getName() {
        return "yaml";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("yamlConfig", new YamlTemplate("src/main/resources/application.yml", commandContext.getConfiguration()));
    }
}
