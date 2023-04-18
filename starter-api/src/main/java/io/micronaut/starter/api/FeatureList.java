/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Models a list of features.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Schema(description = "A list of features")
@Introspected
public class FeatureList extends Linkable {
    private final List<FeatureDTO> features;

    /**
     *
     * @param features A list of features.
     */
    public FeatureList(List<FeatureDTO> features) {
        this.features = features;
    }

    /**
     * @return A list of features.
     */
    @Schema(description = "A list of features")
    public List<FeatureDTO> getFeatures() {
        return features;
    }

}
