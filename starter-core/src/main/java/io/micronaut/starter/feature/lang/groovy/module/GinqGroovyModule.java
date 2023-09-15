/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.lang.groovy.module;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class GinqGroovyModule implements GroovyModuleFeature {

    public static final String NAME = "groovy-ginq";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Groovy Collection Query Extensions";
    }

    @Override
    public String getDescription() {
        return "Extensions for queries against in-memory collections of objects in SQL-like style.";
    }

    @Override
    @NonNull
    public String getThirdPartyDocumentation() {
        return "https://docs.groovy-lang.org/docs/latest/html/documentation/#_querying_collections_in_sql_like_style";
    }
}
