package io.micronaut.starter.openrewrite;

import io.micronaut.starter.sdk.OperatingSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record OpenRewriteConfiguration(List<String> activeRecipes,
                                       boolean exportDatatables,
                                       String recipeChangeLogLevel,
                                       String configLocation,
                                       OperatingSystem operatingSystem) {
    private static final String SYS_PROPERTY_REWRITE_ACTIVE_RECIPES = "rewrite.activeRecipes";
    private static final String SYS_PROPERTY_REWRITE_EXPORT_DATATABLES = "rewrite.exportDatatables";
    private static final String SYS_PROPERTY_REWRITE_RECIPE_CHANGE_LOG_LEVEL = "rewrite.recipeChangeLogLevel";
    private static final String SYS_PROPERTY_REWRITE_CONFIG_LOCATION = "rewrite.configLocation";

    public List<String> getSystemPropertiesList() {
        Map<String, Object> systemProperties = getSystemProperties();
        return systemProperties.keySet()
                .stream()
                .map(k -> "-D" + k + "=" + systemProperties.get(k))
                .toList();
    }

    public Map<String, Object> getSystemProperties() {
        return Map.of(SYS_PROPERTY_REWRITE_ACTIVE_RECIPES, String.join(",", activeRecipes),
                SYS_PROPERTY_REWRITE_EXPORT_DATATABLES, exportDatatables,
                SYS_PROPERTY_REWRITE_RECIPE_CHANGE_LOG_LEVEL, recipeChangeLogLevel,
                SYS_PROPERTY_REWRITE_CONFIG_LOCATION, configLocation);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<String> activeRecipes = new ArrayList<>();
        private boolean exportDatatables;
        private String recipeChangeLogLevel;
        private String configLocation;
        private OperatingSystem operatingSystem;

        public Builder activeRecipe(String recipe) {
            activeRecipes.add(recipe);
            return this;
        }

        public Builder exportDatatables(boolean exportDatatables) {
            this.exportDatatables = exportDatatables;
            return this;
        }

        public Builder recipeChangeLogLevel(String recipeChangeLogLevel) {
            this.recipeChangeLogLevel = recipeChangeLogLevel;
            return this;
        }

        public Builder configLocation(String configLocation) {
            this.configLocation = configLocation;
            return this;
        }

        public Builder activeRecipes(List<String> recipes) {
            this.activeRecipes.addAll(recipes);
            return this;
        }

        public OpenRewriteConfiguration build() {
            return new OpenRewriteConfiguration(activeRecipes,
                    exportDatatables,
                    recipeChangeLogLevel,
                    configLocation,
                    Objects.requireNonNull(operatingSystem));
        }

        public Builder operatingSystem(OperatingSystem operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }
    }
}
