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
package io.micronaut.starter.feature.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.build.gradle.Dockerfile;
import io.micronaut.starter.feature.build.gradle.MicronautApplicationGradlePlugin;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.starter.feature.graalvm.GraalVM.FEATURE_NAME_GRAALVM;

@Singleton
public class MicronautBuildPlugin implements BuildPluginFeature {

    @Override
    @NonNull
    public String getName() {
        return "micronaut-build";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(shouldApplyMicronautApplicationGradlePlugin(generatorContext) ?
                    micronautGradleApplicationPluginBuilder(generatorContext).build() :
                    micronautLibraryGradlePlugin(generatorContext));
        }
    }

    Optional<String> resolveRuntime(GeneratorContext generatorContext) {
        return generatorContext.getBuildProperties()
                .getProperties()
                .stream()
                .filter(property -> MicronautRuntimeFeature.PROPERTY_MICRONAUT_RUNTIME.equals(property.getKey()))
                .map(Property::getValue)
                .findFirst();
    }

    protected MicronautApplicationGradlePlugin.Builder micronautGradleApplicationPluginBuilder(GeneratorContext generatorContext, String id) {
        MicronautApplicationGradlePlugin.Builder builder = MicronautApplicationGradlePlugin.builder()
                .buildTool(generatorContext.getBuildTool())
                .incremental(true)
                .packageName(generatorContext.getProject().getPackageName());
        Optional<GradleDsl> gradleDsl = generatorContext.getBuildTool().getGradleDsl();
        if (gradleDsl.isPresent()) {
            builder = builder.dsl(gradleDsl.get());
        }

        Optional<String> runtimeOptional = resolveRuntime(generatorContext);
        if (runtimeOptional.isPresent()) {
            builder = builder.runtime(runtimeOptional.get());
        }
        Optional<String> testRuntimeOptional = resolveTestRuntime(generatorContext);
        if (testRuntimeOptional.isPresent()) {
            builder = builder.testRuntime(testRuntimeOptional.get());
        }
        if (generatorContext.getFeatures().contains(MicronautAot.FEATURE_NAME_AOT)) {
            Coordinate coordinate = generatorContext.resolveCoordinate("micronaut-aot-core");
            builder.aot(coordinate.getVersion());
        }
        return builder.id(id);
    }

    protected MicronautApplicationGradlePlugin.Builder micronautGradleApplicationPluginBuilder(GeneratorContext generatorContext) {
        MicronautApplicationGradlePlugin.Builder builder = micronautGradleApplicationPluginBuilder(generatorContext, MicronautApplicationGradlePlugin.Builder.APPLICATION);
        if (generatorContext.getFeatures().contains(AwsLambda.FEATURE_NAME_AWS_LAMBDA) && (
                (generatorContext.getApplicationType() == ApplicationType.FUNCTION && generatorContext.getFeatures().contains(FEATURE_NAME_GRAALVM)) ||
                (generatorContext.getApplicationType() == ApplicationType.DEFAULT))) {
            builder = builder.dockerNative(Dockerfile.builder().arg("-XX:MaximumHeapSizePercent=80")
                    .arg("-Dio.netty.allocator.numDirectArenas=0")
                    .arg("-Dio.netty.noPreferDirect=true")
                    .build());
        }
        return builder;
    }

    private Optional<String> resolveTestRuntime(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().testFramework().isJunit()) {
            return Optional.of("junit5");
        } else if (generatorContext.getFeatures().testFramework().isKotlinTestFramework()) {
            return Optional.of("kotest");
        } else if (generatorContext.getFeatures().testFramework().isSpock()) {
            return Optional.of("spock2");
        }

        return Optional.empty();
    }

    protected GradlePlugin micronautLibraryGradlePlugin(GeneratorContext generatorContext) {
        return micronautGradleApplicationPluginBuilder(generatorContext, MicronautApplicationGradlePlugin.Builder.LIBRARY).build();
    }

    private static boolean shouldApplyMicronautApplicationGradlePlugin(GeneratorContext generatorContext) {
        return generatorContext.getFeatures().mainClass().isPresent() ||
                generatorContext.getFeatures().contains("oracle-function") ||
                generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getFeatures().contains("aws-lambda");
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
