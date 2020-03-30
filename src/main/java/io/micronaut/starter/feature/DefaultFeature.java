package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;

import java.util.List;

public interface DefaultFeature extends Feature {

    boolean shouldApply(MicronautCommand micronautCommand,  List<Feature> selectedFeatures);
}
