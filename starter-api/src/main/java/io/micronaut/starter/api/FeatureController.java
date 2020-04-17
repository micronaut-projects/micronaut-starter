package io.micronaut.starter.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.feature.Feature;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/features")
public class FeatureController implements FeatureOperations {

    private final List<Feature> features;

    public FeatureController(List<Feature> features) {
        this.features = features;
    }

    @Override
    @Get("/")
    public List<FeatureDTO> features() {
        return features.stream()
                .map(feature ->
                        new FeatureDTO(
                                feature.getName(),
                                feature.getDescription()
                        )
                ).collect(Collectors.toList());
    }
}
