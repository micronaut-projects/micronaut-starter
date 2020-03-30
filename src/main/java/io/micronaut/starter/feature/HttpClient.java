package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class HttpClient implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, List<Feature> selectedFeatures) {
        return micronautCommand.getName().equals("create-app");
    }

    @Override
    public String getName() {
        return "http-client";
    }
}
