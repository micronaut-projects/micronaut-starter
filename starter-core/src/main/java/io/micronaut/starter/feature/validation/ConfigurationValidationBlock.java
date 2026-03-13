/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.validation;

import io.micronaut.core.annotation.Introspected;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static io.micronaut.starter.util.XmlUtils.appendList;
import static io.micronaut.starter.util.XmlUtils.appendTag;
import static io.micronaut.starter.util.XmlUtils.indent;

/**
 * Represents the {@code <configurationValidation>} block used by the Micronaut Maven plugin.
 *
 * @param enabled Whether configuration validation is enabled
 * @param suppressions Regex patterns for property keys to suppress in validation reports
 * @param suppressInjectErrors Patterns for dependency injection errors to suppress
 * @param failOnNotPresent Whether generation fails when expected configuration is not present
 * @param deduceEnvironments Whether environments should be deduced automatically
 * @param validateDependencyInjection Whether dependency injection wiring should be validated
 * @param format Output format, for example {@code json}, {@code html}, or {@code both}
 * @param outputDirectory Output directory where validation reports are written
 * @param devEnvironments Additional environments for the {@code dev} block
 * @param packageValidationEnvironments Additional environments for the {@code packageValidation} block
 * @param cacheEnabled Whether validation cache is enabled
 * @param testEnvironments Additional environments for the {@code test} block
 * @see <a href="https://micronaut-projects.github.io/micronaut-maven-plugin/latest/examples/configuration-validation.html">Micronaut Maven Plugin Configuration Validation</a>
 */
@NullMarked
@Introspected
public record ConfigurationValidationBlock(
        @Nullable
        Boolean enabled,

        @Nullable
        List<String> suppressions,

        @Nullable
        List<String> suppressInjectErrors,

        @Nullable
        Boolean failOnNotPresent,

        @Nullable
        Boolean deduceEnvironments,

        @Nullable
        Boolean validateDependencyInjection,

        @Nullable
        String format,

        @Nullable
        String outputDirectory,

        @Nullable
        List<String> devEnvironments,

        @Nullable
        List<String> packageValidationEnvironments,

        @Nullable
        Boolean cacheEnabled,

        @Nullable
        List<String> testEnvironments
) {

    private static final int INDENT_LEVEL = 1;

    /**
     * Serializes this configuration into the XML fragment expected by the plugin.
     *
     * @return XML for a {@code <configurationValidation>} element
     */
    public @NonNull String toXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<configurationValidation>\n");

        if (enabled != null) {
            appendTag(xml, "enabled", enabled.toString(), INDENT_LEVEL);
            xml.append('\n');
        }

        appendList(xml, "suppressions", "suppression", suppressions, INDENT_LEVEL, false);
        appendList(xml, "suppressInjectErrors", "suppressInjectError", suppressInjectErrors, INDENT_LEVEL, false);

        if (failOnNotPresent != null) {
            appendTag(xml, "failOnNotPresent", failOnNotPresent.toString(), INDENT_LEVEL);
        }
        if (deduceEnvironments != null) {
            appendTag(xml, "deduceEnvironments", deduceEnvironments.toString(), INDENT_LEVEL);
        }
        if (validateDependencyInjection != null) {
            appendTag(xml, "validateDependencyInjection", validateDependencyInjection.toString(), INDENT_LEVEL);
        }
        if (format != null) {
            appendTag(xml, "format", format, INDENT_LEVEL);
        }
        if (outputDirectory != null) {
            appendTag(xml, "outputDirectory", outputDirectory, INDENT_LEVEL);
        }
        if (cacheEnabled != null) {
            appendTag(xml, "cacheEnabled", cacheEnabled.toString(), INDENT_LEVEL);
        }
        if (failOnNotPresent != null || deduceEnvironments != null || validateDependencyInjection != null || format != null || outputDirectory != null || cacheEnabled != null) {
            xml.append('\n');
        }

        appendEnvironmentSection(xml, "dev", devEnvironments);
        appendEnvironmentSection(xml, "packageValidation", packageValidationEnvironments);
        appendEnvironmentSection(xml, "test", testEnvironments);

        xml.append("</configurationValidation>");
        return xml.toString();
    }

    private static void appendEnvironmentSection(@NonNull StringBuilder xml,
                                                 @NonNull String sectionName,
                                                 @Nullable List<@NonNull String> environments) {
        if (environments == null) {
            return;
        }
        xml.append(indent(INDENT_LEVEL)).append('<').append(sectionName).append(">\n");
        appendList(xml, "environments", "environment", environments, INDENT_LEVEL + 1, true);
        xml.append(indent(INDENT_LEVEL)).append("</").append(sectionName).append(">\n");
    }

    /**
     * @return A builder for {@link ConfigurationValidationBlock}
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean enabled;
        private List<String> suppressions;
        private List<String> suppressInjectErrors;
        private Boolean failOnNotPresent;
        private Boolean deduceEnvironments;
        private Boolean validateDependencyInjection;
        private String format;
        private String outputDirectory;
        private List<String> devEnvironments;
        private List<String> packageValidationEnvironments;
        private Boolean cacheEnabled;
        private List<String> testEnvironments;

        public @Nullable Builder enabled(@Nullable Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public @Nullable Builder suppressions(@Nullable List<@NonNull String> suppressions) {
            this.suppressions = suppressions;
            return this;
        }

        public @Nullable Builder suppressInjectErrors(@Nullable List<@NonNull String> suppressInjectErrors) {
            this.suppressInjectErrors = suppressInjectErrors;
            return this;
        }

        public @Nullable Builder failOnNotPresent(@Nullable Boolean failOnNotPresent) {
            this.failOnNotPresent = failOnNotPresent;
            return this;
        }

        public @Nullable Builder failOnNotPresent() {
            return failOnNotPresent(true);
        }

        public @Nullable Builder deduceEnvironments(@Nullable Boolean deduceEnvironments) {
            this.deduceEnvironments = deduceEnvironments;
            return this;
        }

        public @Nullable Builder deduceEnvironments() {
            return deduceEnvironments(true);
        }

        public @Nullable Builder validateDependencyInjection(@Nullable Boolean validateDependencyInjection) {
            this.validateDependencyInjection = validateDependencyInjection;
            return this;
        }

        public @Nullable Builder validateDependencyInjection() {
            return validateDependencyInjection(true);
        }

        public @Nullable Builder format(@Nullable String format) {
            this.format = format;
            return this;
        }

        public @Nullable Builder outputDirectory(@Nullable String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public @Nullable Builder devEnvironments(@Nullable List<@NonNull String> devEnvironments) {
            this.devEnvironments = devEnvironments;
            return this;
        }

        public @Nullable Builder packageValidationEnvironments(@Nullable List<@NonNull String> packageValidationEnvironments) {
            this.packageValidationEnvironments = packageValidationEnvironments;
            return this;
        }

        public @Nullable Builder cacheEnabled(@Nullable Boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        public @Nullable Builder cacheEnabled() {
            return cacheEnabled(true);
        }

        public @Nullable Builder testEnvironments(@Nullable List<@NonNull String> testEnvironments) {
            this.testEnvironments = testEnvironments;
            return this;
        }

        public @NonNull ConfigurationValidationBlock build() {
            return new ConfigurationValidationBlock(
                    enabled,
                    suppressions,
                    suppressInjectErrors,
                    failOnNotPresent,
                    deduceEnvironments,
                    validateDependencyInjection,
                    format,
                    outputDirectory,
                    devEnvironments,
                    packageValidationEnvironments,
                    cacheEnabled,
                    testEnvironments
            );
        }
    }
}
