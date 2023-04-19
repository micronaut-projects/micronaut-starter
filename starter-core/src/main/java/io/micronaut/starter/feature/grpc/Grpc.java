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
package io.micronaut.starter.feature.grpc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.grpc.template.proto;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;

@Singleton
public class Grpc implements DefaultFeature {
    private final static Dependency DEPENDENCY_MICRONAUT_GRPC_RUNTIME = MicronautDependencyUtils.grpcDependency()
            .artifactId("micronaut-grpc-runtime")
            .compile()
            .build();

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.GRPC;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        generatorContext.addTemplate("proto", new RockerTemplate("src/main/proto/{propertyName}.proto", proto.template(generatorContext.getProject())));
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addHelpLink("Protobuf Gradle Plugin", "https://plugins.gradle.org/plugin/com.google.protobuf");
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
        }
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_GRPC_RUNTIME);
    }

    private BuildPlugin gradlePlugin(GeneratorContext generatorContext) {
        GradlePlugin.Builder builder = GradlePlugin.builder()
                .id("com.google.protobuf")
                .lookupArtifactId("protobuf-gradle-plugin");
        Optional<GradleDsl> gradleDslOptional = generatorContext.getBuildTool().getGradleDsl();
        if (gradleDslOptional.isPresent() && gradleDslOptional.get() == GradleDsl.KOTLIN) {
            builder.buildImports("import com.google.protobuf.gradle.*");
        }
        return builder.build();
    }

    @Override
    public String getName() {
        return "grpc";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }
}
