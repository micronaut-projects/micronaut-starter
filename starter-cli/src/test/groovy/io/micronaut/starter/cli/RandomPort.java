package io.micronaut.starter.cli;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.Set;

@Singleton
public class RandomPort implements DefaultFeature {

    @Override
    public String getName() {
        return "random-port";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.server.port", "-1");
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

}
