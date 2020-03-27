package io.micronaut.starter.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Features extends ArrayList<String> {

    private final List<Feature> featureList;
    private final ApplicationFeature applicationFeature;

    public Features(List<Feature> featureList) {
        super(featureList.stream().map(Feature::getName).collect(Collectors.toList()));
        this.featureList = featureList;
        this.applicationFeature = featureList.stream()
                .filter(feature -> feature instanceof ApplicationFeature)
                .map(ApplicationFeature.class::cast)
                .findFirst().orElse(null);
    }

    public ApplicationFeature application() {
        return applicationFeature;
    }

}
