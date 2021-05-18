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
package io.micronaut.starter.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.Writable;

public interface BuildPlugin extends Ordered {

    @NonNull
    BuildTool getBuildTool();

    @Nullable
    Writable getExtension();

    boolean requiresLookup();

    BuildPlugin resolved(CoordinateResolver coordinateResolver);
}
