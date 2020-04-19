/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import io.micronaut.context.BeanLocator;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import io.micronaut.starter.command.CreateGrpcCommand;
import io.micronaut.starter.feature.Feature;

import java.util.Collections;
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
    private final BeanLocator beanLocator;

    /**
     * Default constructor.
     * @param features The features
     */
    public FeatureController(List<Feature> features, BeanLocator beanLocator) {
        this.features = features;
        this.beanLocator = beanLocator;
    }

    @Override
    @Get("/")
    public List<FeatureDTO> features() {
        return features.stream()
                .map(FeatureDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeatureDTO> features(ApplicationTypes type) {
        switch (type) {
            case app:
                return beanLocator.getBean(CreateAppCommand.CreateAppFeatures.class)
                        .getAllFeatures()
                        .map(FeatureDTO::new)
                        .collect(Collectors.toList());
            case grpc:
                return beanLocator.getBean(CreateGrpcCommand.CreateGrpcFeatures.class)
                        .getAllFeatures()
                        .map(FeatureDTO::new)
                        .collect(Collectors.toList());
            case cli:
                return beanLocator.getBean(CreateCliCommand.CreateCliFeatures.class)
                        .getAllFeatures()
                        .map(FeatureDTO::new)
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }
}
