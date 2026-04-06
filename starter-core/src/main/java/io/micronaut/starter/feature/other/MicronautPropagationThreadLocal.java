package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

import java.util.Set;

@Singleton
class MicronautPropagationThreadLocal implements DefaultFeature {
    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    @NonNull
    public String getName() {
        return "micronaut-propagation-thread-local";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.propagation", "thread-local");
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
