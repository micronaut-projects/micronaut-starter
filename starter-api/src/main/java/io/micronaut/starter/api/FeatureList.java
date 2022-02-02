/*
 * Copyright 2017-2020 original authors
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

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Models a list of features.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Schema(description = "A list of features")
@Serdeable
public class FeatureList extends Linkable {
    private List<FeatureDTO> features;

    /**
     * Constructor.
     */
    public FeatureList() {

    }

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

    /**
     *
     * @param features a list of features.
     */
    public void setFeatures(List<FeatureDTO> features) {
        this.features = features;
    }
}
