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
package io.micronaut.starter.api.options;

import io.micronaut.starter.api.BuildToolDTO;
import io.micronaut.starter.api.SelectOptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "BuildToolSelectOptions")
public class BuildToolSelectOptions extends SelectOptionDTO<BuildToolDTO> {
    public BuildToolSelectOptions(List<BuildToolDTO> options, BuildToolDTO defaultOption) {
        super(options, defaultOption);
    }

    @Override
    public List<BuildToolDTO> getOptions() {
        return super.getOptions();
    }

    @Override
    public BuildToolDTO getDefaultOption() {
        return super.getDefaultOption();
    }
}
