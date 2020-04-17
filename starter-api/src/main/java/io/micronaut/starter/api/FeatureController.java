package io.micronaut.starter.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.feature.Feature;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the {@link FeatureOperations} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/features")
public class FeatureController implements FeatureOperations {

    private final List<Feature> features;

    /**
     * Default constructor.
     * @param features The features
     */
    public FeatureController(List<Feature> features) {
        this.features = features;
    }

    @Override
    @Get("/")
    public List<FeatureDTO> features() {
        return features.stream()
                .map(FeatureDTO::new)
                .collect(Collectors.toList());
    }
}
