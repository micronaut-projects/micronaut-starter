package io.micronaut.starter.feature;

import io.micronaut.context.BeanContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.jdbc.Dbcp;
import io.micronaut.starter.feature.jdbc.Hikari;
import io.micronaut.starter.feature.jdbc.Tomcat;
import io.micronaut.starter.feature.logging.Log4j2;
import io.micronaut.starter.feature.logging.Logback;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AvailableFeatures implements Iterable<String> {

    private static Map<String, Feature> FEATURES;

    static {
        FEATURES = new HashMap<>(16);
        BeanContext ctx = BeanContext.run();
        ctx.getBeansOfType(Feature.class).forEach(AvailableFeatures::add);
        ctx.close();
    }

    public MicronautCommand getCommand() {
        return command;
    }

    private final MicronautCommand command;

    private static void add(Feature feature) {
        FEATURES.put(feature.getName(), feature);
    }

    public static List<Feature> list() {
        return FEATURES.values()
                .stream()
                .filter(Feature::isVisible)
                .collect(Collectors.toList());
    }

    public AvailableFeatures(MicronautCommand command) {
        this.command = command;
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
        Feature feature = FEATURES.get(name);
        if (feature != null) {
            if ((ignoreVisibility || feature.isVisible()) && feature.supports(command.getName())) {
                return Optional.of(feature);
            }
        }
        return Optional.empty();
    }

    public Stream<Feature> getFeatures() {
        Stream<Feature> stream = FEATURES.values().stream();
        return stream
                .filter(Feature::isVisible)
                .filter(feature -> feature.supports(command.getName()));
    }
}
