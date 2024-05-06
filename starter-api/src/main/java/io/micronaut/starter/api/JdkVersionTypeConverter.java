/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import io.micronaut.starter.options.JdkVersion;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Type converter required to convert strings such as "JDK_21" to JdkVersion objects for api REST calls.
 */
@Singleton
public class JdkVersionTypeConverter implements TypeConverter<String, JdkVersion> {
    @Override
    public Optional<JdkVersion> convert(String object, Class<JdkVersion> targetType, ConversionContext context) {
        return Optional.of(JdkVersion.valueOf(object));
    }
}
