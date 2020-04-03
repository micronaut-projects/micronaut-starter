package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class HttpClient implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures) {
        return micronautCommand == MicronautCommand.CREATE_APP;
    }

    @Override
    public String getName() {
        return "http-client";
    }
}
