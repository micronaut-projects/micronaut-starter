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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.discovery.DiscoveryCore;
import io.micronaut.starter.feature.grpc.template.proto;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;

@Requires(property = "micronaut.starter.feature.grpc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Grpc implements DefaultFeature {
    private static final Dependency DEPENDENCY_JAVAX_ANNOTATION_API = Dependency.builder()
            .groupId("javax.annotation")
            .artifactId("javax.annotation-api")
            .compile()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_GRPC_RUNTIME = MicronautDependencyUtils.grpcDependency()
            .artifactId("micronaut-grpc-runtime")
            .compile()
            .build();

    private final DiscoveryCore discoveryCore;

    public Grpc(DiscoveryCore discoveryCore) {
        this.discoveryCore = discoveryCore;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(DiscoveryCore.class, discoveryCore);
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.GRPC;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        generatorContext.addTemplate("proto", new RockerTemplate("src/main/proto/{propertyName}.proto", proto.template(generatorContext.getProject())));
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.addHelpLink("Protobuf Gradle Plugin", "https://plugins.gradle.org/plugin/com.google.protobuf");
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
        }
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_GRPC_RUNTIME);
        generatorContext.addDependency(DEPENDENCY_JAVAX_ANNOTATION_API);
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
    public String getCategory() {
        return Category.API;
    }
}
