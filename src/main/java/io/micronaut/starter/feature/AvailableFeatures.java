package io.micronaut.starter.feature;

import java.util.*;
import java.util.stream.Stream;

public abstract class AvailableFeatures implements Iterable<String> {

    private final Map<String, Feature> features;

    public AvailableFeatures(List<Feature> features) {
        this.features = new LinkedHashMap<>(features.size());
        for (Feature feature: features) {
            this.features.put(feature.getName(), feature);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return getFeatures()
                .map(Feature::getName)
                .iterator();
    }

    public Optional<Feature> findFeature(String name) {
        return findFeature(name, false);
    }

    public Optional<Feature> findFeature(String name, boolean ignoreVisibility) {
        Feature feature = features.get(name);
        if (feature != null) {
            if (ignoreVisibility || feature.isVisible()) {
                return Optional.of(feature);
            }
        }
        return Optional.empty();
    }

    public Stream<Feature> getFeatures() {
        Stream<Feature> stream = features.values().stream();
        return stream.filter(Feature::isVisible);
    }
}
