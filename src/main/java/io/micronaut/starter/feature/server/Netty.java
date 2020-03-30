package io.micronaut.starter.feature.server;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Netty implements ServerFeature, DefaultFeature {

    @Override
    public String getName() {
        return "netty-server";
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, List<Feature> selectedFeatures) {
        return micronautCommand.getName().equals("create-app") &&
                selectedFeatures.stream().noneMatch(f -> f instanceof ServerFeature);
    }
}
