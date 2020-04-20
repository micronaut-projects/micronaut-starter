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

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.application.ApplicationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO objects for {@link ApplicationType}.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Schema(name = "ApplicationTypeInfo")
@Introspected
public class ApplicationTypeDTO extends Linkable {

    private final ApplicationType type;
    private final List<FeatureDTO> features;

    /**
     * @param type The type
     * @param features The available features
     */
    public ApplicationTypeDTO(ApplicationType type, List<FeatureDTO> features) {
        this.type = type;
        this.features = features;
    }

    @Schema(description = "The title of the application type")
    public String getTitle() {
        return type.getTitle();
    }

    @Schema(description = "The possible application features")
    public List<FeatureDTO> getFeatures() {
        return features;
    }

    @Schema(description = "A description of the application type")
    public String getDescription() {
        return type.getDescription();
    }

    @Schema(description = "The name of the application type")
    @NonNull
    public String getName() {
        return type.getName();
    }

}
