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
package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.KotlinSpecificFeature;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class Mockk implements MockingFeature, KotlinSpecificFeature {
    public static final String ARTIFACT_ID_MOCKK = "mockk";
    public static final String NAME_MOCKK = "mockk";
    public static final String GROUP_ID_IO_MOCKK = "io.mockk";

    @Override
    @NonNull
    public String getName() {
        return NAME_MOCKK;
    }

    @Override
    public String getTitle() {
        return "Mockk";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Mocking library for Kotlin";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://mockk.io";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId(GROUP_ID_IO_MOCKK)
                    .artifactId(ARTIFACT_ID_MOCKK)
                    .test());
        }
    }
}


