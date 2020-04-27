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

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.naming.Described;
import io.micronaut.core.naming.Named;
import io.micronaut.starter.feature.Feature;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Represents an application feature.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
@Schema(name = "Feature")
public class FeatureDTO implements Named, Described {
    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".features.";
    private final String name;
    private final String title;
    private final String description;
    private final String category;

    /**
     * Default constructor.
     * @param feature The feature
     * @param messageSource The message source
     * @param messageContext The messageContext
     */
    public FeatureDTO(Feature feature, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        this.name = feature.getName();
        this.title = messageSource.getMessage(MESSAGE_PREFIX + this.name + ".title", messageContext, feature.getTitle());
        this.description = messageSource.getMessage(MESSAGE_PREFIX + this.name + ".description", messageContext, feature.getDescription());
        this.category = feature.getCategory().getName();
    }

    /**
     * Default constructor.
     * @param name The name
     * @param title The title
     * @param description The description
     * @param category
     */
    @Creator
    public FeatureDTO(String name, String title, String description, String category) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    /**
     * @return The name of the feature
     */
    @Schema(description = "The name of the feature")
    public String getName() {
        return name;
    }

    /**
     * @return The title of the feature
     */
    @Schema(description = "The title of the feature")
    public String getTitle() {
        return title;
    }

    /**
     * @return The description of the feature
     */
    @Schema(description = "A description of the feature")
    public String getDescription() {
        return description;
    }

    /**
     * @return The category to which this feature belongs to
     */
    @Schema(description = "The category to which this feature belongs to")
    public String getCategory() {
        return category;
    }
}
