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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class MicroStream implements MicroStreamFeature {

    public static final String NAME = "microstream";
    public static final String MICRONAUT_MICROSTREAM_ANNOTATIONS_ARTIFACT = "micronaut-microstream-annotations";
    public static final String MICRONAUT_MICROSTREAM_VERSION = "micronaut.microstream.version";
    public static final String ARTIFACT_ID_MICRONAUT_MICROSTREAM = "micronaut-microstream";
    private static final Dependency DEPENDENCY_MICRONAUT_MICROSTREAM = MicronautDependencyUtils.microstreamDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_MICROSTREAM)
            .compile()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_MICROSTREAM_ANNOTATIONS = MicronautDependencyUtils.microstreamDependency()
            .artifactId(MICRONAUT_MICROSTREAM_ANNOTATIONS_ARTIFACT)
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
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
        addDependencies(generatorContext);
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_MICROSTREAM);
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_MICROSTREAM_ANNOTATIONS);
        generatorContext.addDependency(MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(),
                MicronautDependencyUtils.GROUP_ID_MICRONAUT_MICROSTREAM, MICRONAUT_MICROSTREAM_ANNOTATIONS_ARTIFACT, MICRONAUT_MICROSTREAM_VERSION));
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
