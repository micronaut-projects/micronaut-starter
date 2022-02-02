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

import io.micronaut.core.annotation.Creator;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Supported Option
 * @param <T> The underlying option type
 */
@Serdeable
public abstract class SelectOptionDTO<T extends Selectable<?>> {

    /**
     * The list of options
     */
    List<T> options;

    /**
     * The Default option
     */
    T defaultOption;

    @Creator
    public SelectOptionDTO(List<T> options, T defaultOption) {
        this.options = options;
        this.defaultOption = defaultOption;
    }

    @ArraySchema(schema =
        @Schema(description = "the supported options")
    )
    public List<T> getOptions() {
        return options;
    }

    @Schema(description = "the default value")
    public T getDefaultOption() {
        return defaultOption;
    }
}
