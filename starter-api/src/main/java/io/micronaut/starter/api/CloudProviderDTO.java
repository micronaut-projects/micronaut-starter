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

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.feature.function.CloudProvider;
import io.micronaut.starter.util.NameUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO objects for {@link CloudProvider}.
 *
 * @since 3.5.0
 */
@Introspected
@Schema(name = "CloudProviderInfo")
public class CloudProviderDTO implements Selectable<CloudProvider> {

    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".cloudProviders.";

    CloudProvider value;
    String label;
    String description;

    public CloudProviderDTO(CloudProvider cloudProvider) {
        this.value = cloudProvider;
        this.label = cloudProvider.getName();
        this.description = cloudProvider.getName();
    }

    @Internal
    public CloudProviderDTO(CloudProvider cloudProvider, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        this.value = cloudProvider;
        this.label = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".label", messageContext, cloudProvider.getName());
        this.description = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".description", messageContext, cloudProvider.getName());
    }

    @Creator
    public CloudProviderDTO(CloudProvider value, String label) {
        this.value = value;
        this.label = label;
        this.description = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public CloudProvider getValue() {
        return value;
    }
}
