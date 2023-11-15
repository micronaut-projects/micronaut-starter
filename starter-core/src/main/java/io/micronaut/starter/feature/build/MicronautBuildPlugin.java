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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.version.SemanticVersion;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.S01SonatypeSnapshots;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.gradle.GradlePluginPortal;
import io.micronaut.starter.build.gradle.GradleRepository;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.build.gradle.Dockerfile;
import io.micronaut.starter.feature.build.gradle.MicronautApplicationGradlePlugin;
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.HibernateReactiveFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.function.LambdaRuntimeMainClass;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.starter.feature.security.SecurityJWT;
import io.micronaut.starter.feature.security.SecurityOAuth2;
import io.micronaut.starter.feature.testresources.DbType;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import java.util.Optional;
import java.util.Set;

import static io.micronaut.starter.feature.graalvm.GraalVM.FEATURE_NAME_GRAALVM;
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.ARTIFACT_ID_MICRONAUT_DATA_PROCESSOR_ARTIFACT;

@Singleton
public class MicronautBuildPlugin implements BuildPluginFeature, DefaultFeature {
    public static final String MICRONAUT_GRADLE_DOCS_URL = "https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/";
    public static final String GRAALVM_GRADLE_DOCS_URL = "https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html";
    public static final String AOT_KEY_SECURITY_JWKS = "micronaut.security.jwks.enabled";
    public static final String AOT_KEY_SECURITY_OPENID = "micronaut.security.openid-configuration.enabled";

    protected final CoordinateResolver coordinateResolver;

    public MicronautBuildPlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

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
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
        }
    }
    
    @NonNull
    protected GradlePlugin gradlePlugin(@NonNull GeneratorContext generatorContext) {
        GradlePlugin.Builder builder = null;
        if (shouldApplyMicronautApplicationGradlePlugin(generatorContext)) {
            builder = micronautGradleApplicationPluginBuilder(generatorContext).builder();
        } else {
            builder = micronautLibraryGradlePluginBuilder(generatorContext);
        }

        if (shouldAddRepositoriesForSnapshots(builder)) {
            builder.pluginsManagementRepository(new GradlePluginPortal())
                    .pluginsManagementRepository(GradleRepository.of(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY), new S01SonatypeSnapshots()));
        }
        return builder.build();
    }

    public boolean shouldAddRepositoriesForSnapshots(GradlePlugin.Builder builder) {
        Optional<String> artifactIdOptional = builder.getArtifiactId();
        if (!artifactIdOptional.isPresent()) {
            return false;
        }
        String artifactId = artifactIdOptional.get();
        Optional<Coordinate> coordinateOptional = coordinateResolver.resolve(artifactId);
        if (!coordinateOptional.isPresent()) {
            return false;
        }
        Coordinate coordinate = coordinateOptional.get();
        if (coordinate.getVersion() == null) {
            return false;
        }
        try {
            SemanticVersion semanticVersion = new SemanticVersion(coordinate.getVersion());
            return semanticVersion.getVersion().endsWith("-SNAPSHOT");
        } catch (IllegalArgumentException e) {
            return false;
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

    @Nullable
    private Set<String> ignoredAutomaticDependencies(GeneratorContext generatorContext) {
        if (generatorContext.hasDependency(MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA, MicronautDependencyUtils.ARTIFACT_ID_MICRONAUT_DATA_TX_HIBERNATE)
                && generatorContext.countDependencies(MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA) == 1) {
            return Set.of(MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA + ":" + ARTIFACT_ID_MICRONAUT_DATA_PROCESSOR_ARTIFACT);
        }
        return null;
    }

    protected MicronautApplicationGradlePlugin.Builder micronautGradleApplicationPluginBuilder(GeneratorContext generatorContext, String id) {
        MicronautApplicationGradlePlugin.Builder builder = MicronautApplicationGradlePlugin.builder()
                .buildTool(generatorContext.getBuildTool())
                .incremental(true)
                .packageName(generatorContext.getProject().getPackageName())
                .ignoredAutomaticDependencies(ignoredAutomaticDependencies(generatorContext));
        generatorContext.getFeatures()
                .getFeatures()
                .stream()
                .filter(LambdaRuntimeMainClass.class::isInstance)
                .map(f -> ((LambdaRuntimeMainClass) f).getLambdaRuntimeMainClass())
                .findFirst()
                .ifPresent(builder::lambdaRuntimeMainClass);
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
        if (generatorContext.getFeatures().hasFeature(TestResources.class)) {
            Optional<DatabaseDriverFeature> databaseDriverFeature = generatorContext.getFeatures().getFeature(DatabaseDriverFeature.class);
            if (
                    (!generatorContext.getFeatures().hasFeature(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) &&
                            databaseDriverFeature.isPresent()
            ) {
                databaseDriverFeature.flatMap(DatabaseDriverFeature::getDbType)
                        .map(dbType -> getModuleName(generatorContext, dbType))
                        .ifPresent(builder::addAdditionalTestResourceModules);
            }
            if (generatorContext.getFeatures().isFeaturePresent(SharedTestResourceFeature.class)) {
                builder = builder.withSharedTestResources();
            }
        }
        if (generatorContext.getFeatures().contains(MicronautAot.FEATURE_NAME_AOT)) {
            Coordinate coordinate = generatorContext.resolveCoordinate("micronaut-aot-core");
            builder.aot(coordinate.getVersion());
            if (generatorContext.getFeatures().hasFeature(SecurityJWT.class) || generatorContext.getFeatures().hasFeature(SecurityOAuth2.class)) {
                builder.aotKey(AOT_KEY_SECURITY_JWKS, false);
            }
            if (generatorContext.getFeatures().hasFeature(SecurityOAuth2.class)) {
                builder.aotKey(AOT_KEY_SECURITY_OPENID, false);
            }
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
            builder = builder.dockerNative(Dockerfile.builder().baseImage("amazonlinux:2")
                    .arg("-XX:MaximumHeapSizePercent=80")
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
            return Optional.of("kotest5");
        } else if (generatorContext.getFeatures().testFramework().isSpock()) {
            return Optional.of("spock2");
        }

        return Optional.empty();
    }

    protected GradlePlugin.Builder micronautLibraryGradlePluginBuilder(GeneratorContext generatorContext) {
        return micronautGradleApplicationPluginBuilder(generatorContext, MicronautApplicationGradlePlugin.Builder.LIBRARY).builder();
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

    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
