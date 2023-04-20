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

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.naming.Described;
import io.micronaut.core.naming.Named;
import io.micronaut.starter.application.ApplicationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

/**
 * DTO objects for {@link ApplicationType}.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Schema(name = "ApplicationTypeInfo")
@Introspected
public class ApplicationTypeDTO extends Linkable implements Named, Described, Selectable<ApplicationType> {

    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".application-types.";
    private final String name;
    private final List<FeatureDTO> features;
    private final String title;
    private final String description;
    private final ApplicationType value;

    /**
     * @param type The type
     * @param features The available features
     */
    public ApplicationTypeDTO(ApplicationType type, List<FeatureDTO> features) {
        this(type,
                type.getName(),
                type.getTitle(),
                type.getDescription(),
                features);
    }

    /**
     * @param name the name
     * @param features The available features
     */
    @Creator
    @Internal
    ApplicationTypeDTO(ApplicationType value,
                       String name,
                       String title,
                       String description,
                       @Nullable List<FeatureDTO> features) {
        this.value = value;
        this.name = name;
        this.features = features != null ? features : Collections.emptyList();
        this.title = title;
        this.description = description;
    }

    /**
     * i18n constructor.
     * @param type The type
     * @param features The features
     * @param messageSource The message source
     * @param messageContext The message context
     */
    @Internal
    ApplicationTypeDTO(ApplicationType type, List<FeatureDTO> features, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        this(type,
                type.getName(),
                messageSource.getMessage(MESSAGE_PREFIX + type.getName() + ".title", messageContext, type.getTitle()),
                messageSource.getMessage(MESSAGE_PREFIX + type.getName() + ".description", messageContext, type.getDescription()),
                features
        );
    }

    @Schema(description = "The title of the application type")
    public String getTitle() {
        return title;
    }

    @Schema(description = "The possible application features")
    public List<FeatureDTO> getFeatures() {
        return features;
    }

    @Schema(description = "A description of the application type")
    public String getDescription() {
        return description;
    }

    @Schema(description = "The name of the application type")
    @NonNull
    public String getName() {
        return name;
    }

    @Override
    @Schema(description = "The value of the application type for select options")
    public ApplicationType getValue() {
        return value;
    }

    @Override
    @Schema(description = "The label of the application type for select options")
    public String getLabel() {
        return description
                .replaceFirst("A ", "")
                .trim();
    }
}
