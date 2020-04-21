package io.micronaut.starter.feature;

import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;

import javax.inject.Singleton;
import java.util.List;

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
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getTitle() {
        return "Random Port Support";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.server.port", "-1");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
