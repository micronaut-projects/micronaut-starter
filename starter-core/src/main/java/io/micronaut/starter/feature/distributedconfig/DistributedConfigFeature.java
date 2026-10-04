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
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DistributedConfigFeature extends JvmFeature {

    @Override
    default boolean supports(Language language) {
        return language == Language.PYTHON && supportsPython() || JvmFeature.super.supports(language);
    }

    @Override
    default boolean supports(BuildTool buildTool) {
        return buildTool == BuildTool.PYRONAUT && supportsPython() || JvmFeature.super.supports(buildTool);
    }

    @Override
    default boolean supports(TestFramework testFramework) {
        return testFramework == TestFramework.PYTEST && supportsPython() || JvmFeature.super.supports(testFramework);
    }

    /**
     * Whether this feature generates native configuration imports for Python.
     * @return Whether Python is supported
     */
    default boolean supportsPython() {
        return false;
    }

    /**
     * Adds a native configuration import without replacing imports from other features.
     * @param generatorContext The generator context
     * @param configurationImport The import declaration
     */
    default void addConfigurationImport(GeneratorContext generatorContext, String configurationImport) {
        Map<String, Object> config = generatorContext.getConfiguration();
        Object existing = config.get("micronaut.config.import");
        List<Object> imports = new ArrayList<>();
        if (existing instanceof List<?> list) {
            imports.addAll(list);
        } else if (existing != null) {
            imports.add(existing);
        }
        imports.add(configurationImport);
        config.put("micronaut.config.import", imports);
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI;
    }

    @Override
    default String getCategory() {
        return Category.DISTRIBUTED_CONFIG;
    }

    @NonNull
    default Map<String, Object> populateBootstrapForDistributedConfiguration(@NonNull GeneratorContext generatorContext) {
        Map<String, Object> config = generatorContext.getBootstrapConfiguration();
        config.put("micronaut.application.name", generatorContext.getProject().getPropertyName());
        config.put("micronaut.config-client.enabled", true);
        return config;
    }
}
