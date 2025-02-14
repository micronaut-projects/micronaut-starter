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
package io.micronaut.starter.openrewrite;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;
import org.openrewrite.config.Environment;
import org.openrewrite.config.ResourceLoader;
import org.openrewrite.config.YamlResourceLoader;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.Properties;

@Factory
class ResourceLoaderFactory {
    public static final String CLASSPATH_META_INF_REWRITE_REWRITE_YML = "classpath:META-INF/rewrite/rewrite.yml";

    @Singleton
    ResourceLoader createResourceLoader(ResourceResolver resourceResolver) {
        Optional<InputStream> inputStreamOptional = resourceResolver.getResourceAsStream(CLASSPATH_META_INF_REWRITE_REWRITE_YML);
        if (inputStreamOptional.isEmpty()) {
            throw new ConfigurationException("no " + CLASSPATH_META_INF_REWRITE_REWRITE_YML + " in classpath");
        }
        InputStream inputStream = inputStreamOptional.get();
        return new YamlResourceLoader(inputStream,
                URI.create("rewrite.yml"),
                new Properties()
        );
    }

    @Singleton
    Environment createEnvironment(ResourceLoader resourceLoader) {
        return Environment.builder().load(resourceLoader).build();
    }
}
