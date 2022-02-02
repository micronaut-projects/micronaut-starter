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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A linkable type.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Serdeable
public abstract class Linkable {
    private final Map<String, LinkDTO> links = new LinkedHashMap<>();

    /**
     * @return The links
     */
    @Schema(description = "Links to other resources")
    @JsonProperty("_links")
    @ReflectiveAccess
    public Map<String, LinkDTO> getLinks() {
        return links;
    }

    /**
     * Adds a link.
     * @param rel The relationship
     * @param link The link
     * @return this link
     */
    public Linkable addLink(CharSequence rel, LinkDTO link) {
        if (link != null && rel != null) {
            links.put(rel.toString(), link);
        }
        return this;
    }
}
