/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api.create;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpResponse;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.command.CreateGrpcCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines the signature for creating an application.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface CreateOperation {

    /**
     * Creates an application.
     * @param name The name of the application
     * @param features The features
     * @param buildTool The build tool
     * @param testFramework The test framework
     * @param lang The lang
     * @return An HTTP response that emits a writable
     */
    HttpResponse<Writable> createApp(
            String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang
    );

    /**
     * Builds the create GRPC app command.
     * @param availableFeatures The features
     * @param featureValidator The feature validator
     * @param contextFactory The context factory
     * @param features The features
     * @param lang The lang
     * @param buildTool The build framework
     * @param testFramework The test framework
     * @return The create command
     */
    static CreateGrpcCommand buildCreateGrpcAppCommand(CreateGrpcCommand.CreateGrpcFeatures availableFeatures, FeatureValidator featureValidator, ContextFactory contextFactory, @Nullable List<String> features, Language lang, BuildTool buildTool, TestFramework testFramework) {
        return new CreateGrpcCommand(availableFeatures, featureValidator, contextFactory) {
            @Override
            protected List<String> getSelectedFeatures() {
                return features != null ? features : Collections.emptyList();
            }

            @Nonnull
            @Override
            protected TestFramework getSelectedTestFramework() {
                return testFramework != null ? testFramework : super.getSelectedTestFramework();
            }

            @Nonnull
            @Override
            protected Language getSelectedLang() {
                return lang != null ? lang : super.getSelectedLang();
            }

            @Nonnull
            @Override
            protected BuildTool getSelectedBuildTool() {
                return buildTool != null ? buildTool : super.getSelectedBuildTool();
            }
        };
    }

    /**
     * Builds the create CLI app command.
     * @param availableFeatures The features
     * @param featureValidator The feature validator
     * @param contextFactory The context factory
     * @param features The features
     * @param lang The lang
     * @param buildTool The build framework
     * @param testFramework The test framework
     * @return The create command
     */
    static CreateCliCommand buildCreateCliAppCommand(CreateCliCommand.CreateCliFeatures availableFeatures, FeatureValidator featureValidator, ContextFactory contextFactory, @Nonnull List<String> features, Language lang, BuildTool buildTool, TestFramework testFramework) {
        return new CreateCliCommand(availableFeatures, featureValidator, contextFactory) {
            @Override
            protected List<String> getSelectedFeatures() {
                return features;
            }

            @Nonnull
            @Override
            protected TestFramework getSelectedTestFramework() {
                return testFramework != null ? testFramework : super.getSelectedTestFramework();
            }

            @Nonnull
            @Override
            protected Language getSelectedLang() {
                return lang != null ? lang : super.getSelectedLang();
            }

            @Nonnull
            @Override
            protected BuildTool getSelectedBuildTool() {
                return buildTool != null ? buildTool : super.getSelectedBuildTool();
            }
        };
    }

    /**
     * Builds the create command.
     * @param appFeatures The features
     * @param featureValidator The feature validator
     * @param contextFactory The context factory
     * @param features The features
     * @param lang The lang
     * @param buildTool The build framework
     * @param testFramework The test framework
     * @return The create command
     */
    static CreateCommand buildCreateAppCommand(
            CreateAppCommand.CreateAppFeatures appFeatures,
            FeatureValidator featureValidator,
            ContextFactory contextFactory,
            @Nullable List<String> features,
            Language lang,
            BuildTool buildTool,
            TestFramework testFramework) {
        return new CreateAppCommand(
                appFeatures,
                featureValidator,
                contextFactory

        ) {
            @Override
            protected @Nonnull
            List<String> getSelectedFeatures() {
                return features != null ? features : new ArrayList<>();
            }

            @Nonnull
            @Override
            protected TestFramework getSelectedTestFramework() {
                return testFramework != null ? testFramework : super.getSelectedTestFramework();
            }

            @Nonnull
            @Override
            protected Language getSelectedLang() {
                return lang != null ? lang : super.getSelectedLang();
            }

            @Nonnull
            @Override
            protected BuildTool getSelectedBuildTool() {
                return buildTool != null ? buildTool : super.getSelectedBuildTool();
            }
        };
    }
}
