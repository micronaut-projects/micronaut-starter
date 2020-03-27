package io.micronaut.starter.feature;

import io.micronaut.starter.feature.jdbc.Dbcp;
import io.micronaut.starter.feature.jdbc.Hikari;
import io.micronaut.starter.feature.jdbc.Tomcat;
import io.micronaut.starter.feature.swagger.Swagger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AllFeatures implements Iterable<String> {

    private static Map<String, Feature> FEATURES;

    static {
        FEATURES = new HashMap<>(16);
        add(new Dbcp());
        add(new Hikari());
        add(new Tomcat());
        add(new Swagger());
    }

    private static void add(Feature feature) {
        FEATURES.put(feature.getName(), feature);
    }

    public static List<Feature> list() {
        return FEATURES.values()
                .stream()
                .filter(Feature::isVisible)
                .collect(Collectors.toList());
    }

    public static Feature get(String name) {
        Feature feature = FEATURES.get(name);
        if (feature != null && feature.isVisible()) {
            return feature;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<String> iterator() {
        Stream<Feature> stream = FEATURES.values().stream();

        return apply(stream)
                .filter(Feature::isVisible)
                .map(Feature::getName)
                .iterator();
    }

    protected abstract Stream<Feature> apply(Stream<Feature> stream);

}
