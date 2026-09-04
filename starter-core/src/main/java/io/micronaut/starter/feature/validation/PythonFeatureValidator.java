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

import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.GradleSpecificFeature;
import io.micronaut.starter.feature.KotlinSpecificFeature;
import io.micronaut.starter.feature.MavenSpecificFeature;
import io.micronaut.starter.feature.RequireKaptFeature;
import io.micronaut.starter.feature.RequiresJavaReflection;
import io.micronaut.starter.feature.build.MicronautAot;
import io.micronaut.starter.feature.config.ConfigurationFeature;
import io.micronaut.starter.feature.config.Toml;
import io.micronaut.starter.feature.discovery.DiscoveryKubernetes;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.httpclient.HttpClientJdk;
import io.micronaut.starter.feature.jmx.Jmx;
import io.micronaut.starter.feature.lang.python.PythonApplication;
import io.micronaut.starter.feature.logging.Logback;
import io.micronaut.starter.feature.logging.LoggingFeature;
import io.micronaut.starter.feature.mcp.McpStdio;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.feature.sourcegen.SourcegenJava;
import io.micronaut.starter.feature.test.Pytest;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PythonFeatureValidator implements FeatureValidator {

    private static final String FEATURE_MYBATIS = "mybatis";

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        validateOptions(options);
        if (options.getLanguage() == Language.PYTHON) {
            features.stream()
                    .filter(RequiresJavaReflection.class::isInstance)
                    .findFirst()
                    .ifPresent(PythonFeatureValidator::rejectRequiresJavaReflection);
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        validateOptions(options);
        if (options.getLanguage() != Language.PYTHON) {
            return;
        }
        validateApplicationType(applicationType);
        features.forEach(PythonFeatureValidator::validateFeature);
    }

    private static void validateOptions(Options options) {
        if (options.getLanguage() == Language.PYTHON && options.getBuildTool() != BuildTool.PYRONAUT) {
            throw new IllegalArgumentException("Python applications must use the Pyronaut build tool");
        }
        if (options.getLanguage() == Language.PYTHON
                && options.getTestFramework() != null
                && options.getTestFramework() != TestFramework.PYTEST) {
            throw new IllegalArgumentException("Python applications must use the Pytest test framework");
        }
        if (options.getBuildTool() == BuildTool.PYRONAUT && options.getLanguage() != Language.PYTHON) {
            throw new IllegalArgumentException("The Pyronaut build tool is only supported for Python applications");
        }
        if (options.getTestFramework() == TestFramework.PYTEST && options.getLanguage() != Language.PYTHON) {
            throw new IllegalArgumentException("The Pytest test framework is only supported for Python applications");
        }
    }

    private static void validateApplicationType(ApplicationType applicationType) {
        if (applicationType == ApplicationType.GRPC) {
            throw new IllegalArgumentException("Python applications do not support the %s application type".formatted(applicationType.getName()));
        }
    }

    private static void validateFeature(Feature feature) {
        if (feature instanceof ConfigurationFeature && !(feature instanceof Toml)) {
            unsupported(feature);
        }
        if (feature instanceof LoggingFeature && !(feature instanceof Logback)) {
            unsupported(feature);
        }
        if (feature instanceof GradleSpecificFeature || feature instanceof MavenSpecificFeature
                || feature instanceof KotlinSpecificFeature || feature instanceof RequireKaptFeature) {
            unsupported(feature);
        }
        if (feature instanceof MicronautAot || feature instanceof SourcegenJava
                || feature instanceof HttpClientJdk || feature instanceof Jmx) {
            unsupported(feature);
        }
        if (feature instanceof ServerFeature && !(feature instanceof Netty) && !(feature instanceof McpStdio)) {
            unsupported(feature);
        }
        if (feature instanceof TestFeature && !(feature instanceof Pytest)) {
            unsupported(feature);
        }
        if (feature instanceof RequiresJavaReflection) {
            rejectRequiresJavaReflection(feature);
        }
        if (usesBootstrapConfiguration(feature)) {
            throw new IllegalArgumentException("Feature %s is not supported for Python because it requires bootstrap configuration".formatted(feature.getName()));
        }
        if (feature instanceof CodeContributingFeature && !(feature instanceof PythonApplication)) {
            unsupported(feature);
        }
        if (Category.CICD.equals(feature.getCategory())
                || (Category.PACKAGING.equals(feature.getCategory()) && !(feature instanceof GraalVM))) {
            unsupported(feature);
        }
        if (Category.LOGGING.equals(feature.getCategory()) && !(feature instanceof Logback)) {
            unsupported(feature);
        }
        if (Category.LANGUAGES.equals(feature.getCategory())) {
            unsupported(feature);
        }
        if (Category.TEST.equals(feature.getCategory()) && !(feature instanceof Pytest)) {
            unsupported(feature);
        }
    }

    private static void unsupported(Feature feature) {
        throw new IllegalArgumentException("Feature %s is not supported for Python applications".formatted(feature.getName()));
    }

    private static void rejectRequiresJavaReflection(Feature feature) {
        throw new IllegalArgumentException("Feature %s is not supported for Python because it requires Java reflection".formatted(feature.getName()));
    }

    private static boolean usesBootstrapConfiguration(Feature feature) {
        return feature instanceof DistributedConfigFeature
                || feature instanceof DiscoveryKubernetes;
    }
}
