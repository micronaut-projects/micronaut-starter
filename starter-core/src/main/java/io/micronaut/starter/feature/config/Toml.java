package io.micronaut.starter.feature.config;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TomlTemplate;
import jakarta.inject.Singleton;

import java.util.function.Function;

@Singleton
public class Toml implements ConfigurationFeature {

    private static final String EXTENSION = "toml";

    @Override
    public void apply(GeneratorContext generatorContext) {
        ConfigurationFeature.super.apply(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.toml")
                .artifactId("micronaut-toml")
                .compile());
    }

    @Override
    public String getName() {
        return "toml";
    }

    @Override
    public String getTitle() {
        return "TOML configuration";
    }

    @Override
    public String getDescription() {
        return "Creates a TOML configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return cfg -> new TomlTemplate(cfg.getFullPath(EXTENSION), cfg);
    }
}
