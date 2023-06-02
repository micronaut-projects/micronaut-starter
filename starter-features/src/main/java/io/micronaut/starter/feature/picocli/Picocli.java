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
package io.micronaut.starter.feature.picocli;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.Set;

@Singleton
public class Picocli implements DefaultFeature {

    @Override
    public String getName() {
        return "picocli";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public String getTitle() {
        return "PicoCLI";
    }

    @Override
    public String getDescription() {
        return "Support for creating PicoCLI applications";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("info.picocli")
                .artifactId("picocli-codegen")
                .versionProperty("picocli.version")
                .annotationProcessor());
        generatorContext.addDependency(Dependency.builder().groupId("info.picocli").artifactId("picocli").compile());
        generatorContext.addDependency(MicronautDependencyUtils.picocliDependency().artifactId("micronaut-picocli").compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.CLI;
    }
}
