package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.options.Language;

import java.util.List;

public interface DefaultFeature extends Feature {

    boolean shouldApply(MicronautCommand micronautCommand, Language language, List<Feature> selectedFeatures);
}
