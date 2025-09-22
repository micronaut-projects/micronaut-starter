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
package io.micronaut.starter.cli.util.openrewrite;

import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.core.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @param activeRecipes Active Recipes
 * @param exportDatatables Export Datatables
 * @param recipeChangeLogLevel Recipe Change Log Level
 * @param configLocation Config Location
 * @param operatingSystem Operating System
 */
public record OpenRewriteConfiguration(List<String> activeRecipes,
                                       boolean exportDatatables,
                                       String recipeChangeLogLevel,
                                       String configLocation,
                                       OperatingSystem operatingSystem) {
    private static final String SYS_PROPERTY_REWRITE_ACTIVE_RECIPES = "rewrite.activeRecipes";
    private static final String SYS_PROPERTY_REWRITE_EXPORT_DATATABLES = "rewrite.exportDatatables";
    private static final String SYS_PROPERTY_REWRITE_RECIPE_CHANGE_LOG_LEVEL = "rewrite.recipeChangeLogLevel";
    private static final String SYS_PROPERTY_REWRITE_CONFIG_LOCATION = "rewrite.configLocation";

    /**
     *
     * @return System Properties List
     */
    public List<String> getSystemPropertiesList() {
        Map<String, Object> systemProperties = getSystemProperties();
        return systemProperties.keySet()
                .stream()
                .map(k -> "-D" + k + "=" + systemProperties.get(k))
                .toList();
    }

    /**
     *
     * @return System Properties
     */
    public Map<String, Object> getSystemProperties() {
        Map<String, Object> systemProperties = new HashMap<>();
        if (CollectionUtils.isNotEmpty(activeRecipes)) {
            systemProperties.put(SYS_PROPERTY_REWRITE_ACTIVE_RECIPES, String.join(",", activeRecipes));
        }
        systemProperties.put(SYS_PROPERTY_REWRITE_EXPORT_DATATABLES, exportDatatables);
        systemProperties.put(SYS_PROPERTY_REWRITE_RECIPE_CHANGE_LOG_LEVEL, recipeChangeLogLevel);
        systemProperties.put(SYS_PROPERTY_REWRITE_CONFIG_LOCATION, configLocation);
        return systemProperties;
    }

    /**
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static class Builder {
        private final List<String> activeRecipes = new ArrayList<>();
        private boolean exportDatatables;
        private String recipeChangeLogLevel;
        private String configLocation;
        private OperatingSystem operatingSystem;

        /**
         *
         * @param recipe Recipe
         * @return Builder
         */
        public Builder activeRecipe(String recipe) {
            activeRecipes.add(recipe);
            return this;
        }

        /**
         *
         * @param exportDatatables Export Datatables
         * @return Builder
         */
        public Builder exportDatatables(boolean exportDatatables) {
            this.exportDatatables = exportDatatables;
            return this;
        }

        /**
         *
         * @param recipeChangeLogLevel Recipe Change Log Level
         * @return Builder
         */
        public Builder recipeChangeLogLevel(String recipeChangeLogLevel) {
            this.recipeChangeLogLevel = recipeChangeLogLevel;
            return this;
        }

        /**
         *
         * @param configLocation Config Location
         * @return Builder
         */
        public Builder configLocation(String configLocation) {
            this.configLocation = configLocation;
            return this;
        }

        /**
         *
         * @param recipes recipes
         * @return Builder
         */
        public Builder activeRecipes(List<String> recipes) {
            this.activeRecipes.addAll(recipes);
            return this;
        }

        /**
         *
         * @param operatingSystem Operating System
         * @return Builder
         */
        public Builder operatingSystem(OperatingSystem operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        /**
         *
         * @return OpenRewriteConfiguration
         */
        public OpenRewriteConfiguration build() {
            return new OpenRewriteConfiguration(activeRecipes,
                    exportDatatables,
                    recipeChangeLogLevel,
                    configLocation,
                    Objects.requireNonNull(operatingSystem));
        }
    }
}
