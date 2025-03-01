package io.micronaut.starter.cli;

import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.Options;

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
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.server.port", "-1");
    }
}
