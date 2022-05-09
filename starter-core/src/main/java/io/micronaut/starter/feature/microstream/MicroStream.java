/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.microstream;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicroStream implements Feature {

    public static final String NAME = "microstream";
    private static final String MICRONAUT_MICROSTREAM_GROUP_ID = "io.micronaut.microstream";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getTitle() {
        return "MicroStream";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for using MicroStream with Micronaut";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_MICROSTREAM_GROUP_ID)
                .artifactId("micronaut-microstream")
                .build()
        );
        generatorContext.addDependency(Dependency.builder()
                .compile()
                .groupId(MICRONAUT_MICROSTREAM_GROUP_ID)
                .artifactId("micronaut-microstream-annotations")
                .build()
        );
        generatorContext.addDependency(Dependency.builder()
                .annotationProcessor()
                .groupId(MICRONAUT_MICROSTREAM_GROUP_ID)
                .artifactId("micronaut-microstream-annotations")
                .versionProperty("micronaut.microstream.version")
                .build()
        );
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-microstream/latest/guide";
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://microstream.one/";
    }
}
