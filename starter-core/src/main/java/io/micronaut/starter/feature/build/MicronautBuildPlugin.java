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
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.HibernateReactiveFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.starter.feature.testresources.DbType;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.starter.feature.graalvm.GraalVM.FEATURE_NAME_GRAALVM;

@Singleton
public class MicronautBuildPlugin implements BuildPluginFeature {

    public static final String MICRONAUT_GRADLE_DOCS_URL = "https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/";
    public static final String GRAALVM_GRADLE_DOCS_URL = "https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html";

    @Override
    @NonNull
    public String getName() {
        return "micronaut-build";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {

            generatorContext.addHelpLink("Micronaut Gradle Plugin documentation", MICRONAUT_GRADLE_DOCS_URL);
            if (GraalVMFeatureValidator.supports(generatorContext.getLanguage())) {
                generatorContext.addHelpLink("GraalVM Gradle Plugin documentation", GRAALVM_GRADLE_DOCS_URL);
            }

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
        Optional<DatabaseDriverFeature> databaseDriverFeature = generatorContext.getFeatures().getFeature(DatabaseDriverFeature.class);
        if ((!generatorContext.getFeatures().hasFeature(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class))
                && databaseDriverFeature.isPresent()) {
            databaseDriverFeature.flatMap(DatabaseDriverFeature::getDbType)
                    .map(dbType -> getModuleName(generatorContext, dbType))
                    .ifPresent(builder::addAdditionalTestResourceModules);
        }
        if (generatorContext.getFeatures().isFeaturePresent(SharedTestResourceFeature.class)) {
            builder = builder.withSharedTestResources();
        }
        if (generatorContext.getFeatures().contains(MicronautAot.FEATURE_NAME_AOT)) {
            Coordinate coordinate = generatorContext.resolveCoordinate("micronaut-aot-core");
            builder.aot(coordinate.getVersion());
        }
        return builder.id(id);
    }

    private String getModuleName(GeneratorContext generatorContext, DbType dbType) {
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            return dbType.getR2dbcTestResourcesModuleName();
        } else if (generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) {
            return dbType.getHibernateReactiveTestResourcesModuleName();
        } else {
            return dbType.getJdbcTestResourcesModuleName();
        }
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
