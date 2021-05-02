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
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.NameUtils;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * DTO objects for {@link BuildTool}.
 *
 * @since 2.2.0
 */
@Introspected
@Schema(name = "BuildToolInfo")
public class BuildToolDTO implements Selectable<BuildTool> {
    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".buildTools.";
    BuildTool value;
    String label;
    String description;

    public BuildToolDTO(BuildTool buildTool) {
        this.value = buildTool;
        this.label = buildTool.name();
        this.description = buildTool.name();
    }

    @Internal
    public BuildToolDTO(BuildTool buildTool, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        this.value = buildTool;
        String label = NameUtils.getNaturalNameOfEnum(buildTool.name());
        this.label = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".label", messageContext, label);
        this.description = messageSource.getMessage(MESSAGE_PREFIX + this.value + ".description", messageContext, label);
    }

    @Creator
    public BuildToolDTO(BuildTool value, String label, String description) {
        this.value = value;
        this.label = label;
        this.description = description;
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
    public BuildTool getValue() {
        return value;
    }
}
