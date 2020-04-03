package io.micronaut.starter.feature.server;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Netty implements ServerFeature, DefaultFeature {

    @Override
    public String getName() {
        return "netty-server";
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures) {
        return micronautCommand == MicronautCommand.CREATE_APP &&
                selectedFeatures.stream().noneMatch(f -> f instanceof ServerFeature);
    }
}
