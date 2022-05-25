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
package io.micronaut.starter.api.preview;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.api.Linkable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * A preview of the contents.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
@Schema(name = "Preview", description = "Previews the contents of the generated ZIP")
public class PreviewDTO extends Linkable  {

    @JsonInclude
    private Map<String, String> contents;

    /**
     * Constructor
     */
    public PreviewDTO() {
    }

    /**
     * @param contents The contents
     */
    public PreviewDTO(Map<String, String> contents) {
        this.contents = contents;
    }

    /**
     * @return The contents of the ZIP.
     */
    @Schema(description = "The contents of the generated ZIP")
    public Map<String, String> getContents() {
        return contents;
    }

    /**
     *
     * @param contents The contents of the ZIP
     */
    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }
}
