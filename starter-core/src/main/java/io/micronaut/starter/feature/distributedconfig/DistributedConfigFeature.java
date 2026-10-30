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
package io.micronaut.starter.feature.distributedconfig;

import io.micronaut.starter.feature.JvmFeature;
import org.jspecify.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DistributedConfigFeature extends JvmFeature {

    @Override
    default boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI;
    }

    @Override
    default String getCategory() {
        return Category.DISTRIBUTED_CONFIG;
    }

    @NonNull
    default Map<String, Object> populateConfigurationForDistributedConfiguration(@NonNull GeneratorContext generatorContext) {
        Map<String, Object> config = generatorContext.getConfiguration();
        config.put("micronaut.application.name", generatorContext.getProject().getPropertyName());
        return config;
    }

    /**
     * Adds a scalar or structured config import without replacing another feature's declaration.
     * @param generatorContext The generator context
     * @param declaration The connection string or structured import map
     */
    default void addConfigurationImport(@NonNull GeneratorContext generatorContext, @NonNull Object declaration) {
        Map<String, Object> configuration = populateConfigurationForDistributedConfiguration(generatorContext);
        String key = "micronaut.config.import";
        Object existing = configuration.get(key);
        if (existing == null) {
            configuration.put(key, declaration);
        } else {
            List<Object> imports = existing instanceof List<?> previous
                    ? new ArrayList<>(previous) : new ArrayList<>(List.of(existing));
            imports.add(declaration);
            configuration.put(key, imports);
        }
    }

    @NonNull
    @Deprecated(forRemoval = true)
    default Map<String, Object> populateBootstrapForDistributedConfiguration(@NonNull GeneratorContext generatorContext) {
        return populateConfigurationForDistributedConfiguration(generatorContext);
    }
}
