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

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.maven.templates.aot;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.security.SecurityJWT;
import io.micronaut.starter.feature.security.SecurityOAuth2;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class MicronautAot implements DefaultFeature {
    public static final String FEATURE_NAME_AOT = "micronaut-aot";

    private static final String GRADLE_PLUGIN_ID = "io.micronaut.aot";
    private static final String GRADLE_PLUGIN_ARTIFACT_ID = "micronaut-gradle-plugin";
    private static final int GRADLE_PLUGIN_ORDER = 10;

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AOT;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Build time optimizations to provide faster startup times and smaller binaries.";
    }

    @Override
    @Nullable
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aot/latest/guide/";
    }

    @Override
    public String getTitle() {
        return "Micronaut AOT";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addAotPluginsDependencies(generatorContext);
        if (generatorContext.getBuildTool().isGradle()) {
            addAotGradlePlugin(generatorContext);
        } else {
            addAotBuildProperties(generatorContext);
            addAotConfigurationPropertiesFiles(generatorContext);
        }
    }

    protected void addAotBuildProperties(GeneratorContext generatorContext) {
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        buildProperties.put("micronaut.aot.enabled", StringUtils.FALSE);
        buildProperties.put("micronaut.aot.packageName", generatorContext.getProject().getPackageName() + ".aot.generated");
    }

    protected void addAotGradlePlugin(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id(GRADLE_PLUGIN_ID)
                .lookupArtifactId(GRADLE_PLUGIN_ARTIFACT_ID)
                .order(GRADLE_PLUGIN_ORDER)
                .build());
    }

    protected void addAotConfigurationPropertiesFiles(GeneratorContext generatorContext) {
        List<MicronautAotOptimization> optimizations = optimizations(false, generatorContext);
        RockerModel rockerModel = aot.template("jar", optimizations);
        generatorContext.addTemplate("aotJitProperties", new RockerTemplate("aot-jar.properties", rockerModel));
        if (generatorContext.isFeaturePresent(GraalVM.class)) {
            optimizations = optimizations(true, generatorContext);
            rockerModel = aot.template("native-image", optimizations);
            generatorContext.addTemplate("aotNativeProperties", new RockerTemplate("aot-native-image.properties", rockerModel));
        }
    }

    protected void addAotPluginsDependencies(GeneratorContext generatorContext) {
        if (generatorContext.hasFeature(SecurityJWT.class) || generatorContext.hasFeature(SecurityOAuth2.class)) {
            Dependency.Builder securityAotPluginDependency = MicronautDependencyUtils.securityDependency()
                    .artifactId("micronaut-security-aot")
                    .scope(Scope.AOT_PLUGIN);

            if (generatorContext.getBuildTool().isGradle()) {
                generatorContext.addDependency(MicronautDependencyUtils.platformDependency()
                        .artifactId("micronaut-platform")
                        .version(VersionInfo.getMicronautVersion())
                        .pom()
                        .scope(Scope.AOT_PLUGIN)
                );
                generatorContext.addDependency(securityAotPluginDependency);
            } else {
                generatorContext.addDependency(securityAotPluginDependency.version("${micronaut.security.version}"));
            }
        }
    }

    protected List<MicronautAotOptimization> optimizations(boolean graalvm, GeneratorContext generatorContext) {
        final List<MicronautAotOptimization> optimizations = new ArrayList<>();
        optimizations.add(new MicronautAotOptimization("cached.environment.enabled", true, "Caches environment property values: environment properties will be deemed immutable after application startup."));
        optimizations.add(new MicronautAotOptimization("precompute.environment.properties.enabled", true, "Precomputes Micronaut configuration property keys from the current environment variables"));
        if (graalvm) {
            optimizations.add(new MicronautAotOptimization("yaml.to.java.config.enabled", false, "Converts YAML configuration files to Java configuration"));
            optimizations.add(new MicronautAotOptimization("graalvm.config.enabled", true, "Generates GraalVM configuration files required to load the AOT optimizations"));
            optimizations.add(new MicronautAotOptimization("serviceloading.native.enabled", false, "Scans for service types ahead-of-time, avoiding classpath scanning at startup"));
        } else {
            optimizations.add(new MicronautAotOptimization("yaml.to.java.config.enabled", true, "Converts YAML configuration files to Java configuration"));
            optimizations.add(new MicronautAotOptimization("serviceloading.jit.enabled", true, "Scans for service types ahead-of-time, avoiding classpath scanning at startup"));
        }
        optimizations.add(new MicronautAotOptimization("scan.reactive.types.enabled", true, "Scans reactive types at build time instead of runtime"));
        optimizations.add(new MicronautAotOptimization("deduce.environment.enabled", true, "Deduces the environment at build time instead of runtime"));
        optimizations.add(new MicronautAotOptimization("known.missing.types.enabled", true, "Checks of existence of some types at build time instead of runtime"));
        optimizations.add(new MicronautAotOptimization("sealed.property.source.enabled", true, "Precomputes property sources at build time"));

        optimizations.add(new MicronautAotOptimization("service.types", "io.micronaut.context.env.PropertySourceLoader,io.micronaut.inject.BeanConfiguration,io.micronaut.inject.BeanDefinitionReference,io.micronaut.http.HttpRequestFactory,io.micronaut.http.HttpResponseFactory,io.micronaut.core.beans.BeanIntrospectionReference,io.micronaut.core.convert.TypeConverterRegistrar,io.micronaut.context.env.PropertyExpressionResolver", "The list of service types to be scanned (comma separated)"));

        optimizations.add(new MicronautAotOptimization("known.missing.types.list", "io.reactivex.Observable,reactor.core.publisher.Flux,kotlinx.coroutines.flow.Flow,io.reactivex.rxjava3.core.Flowable,io.reactivex.rxjava3.core.Observable,io.reactivex.Single,reactor.core.publisher.Mono,io.reactivex.Maybe,io.reactivex.rxjava3.core.Single,io.reactivex.rxjava3.core.Maybe,io.reactivex.Completable,io.reactivex.rxjava3.core.Completable,io.methvin.watchservice.MacOSXListeningWatchService,io.micronaut.core.async.publisher.CompletableFuturePublisher,io.micronaut.core.async.publisher.Publishers.JustPublisher,io.micronaut.core.async.subscriber.Completable", "A list of types that the AOT analyzer needs to check for existence (comma separated)"));

        if (generatorContext.hasFeature(SecurityJWT.class) || generatorContext.hasFeature(SecurityOAuth2.class)) {
            optimizations.add(new MicronautAotOptimization("micronaut.security.jwks.enabled", false, "It fetches remote Json Web Key Set at Build Time. https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#aotJwks"));
        }
        if (generatorContext.hasFeature(SecurityOAuth2.class)) {
            optimizations.add(new MicronautAotOptimization("micronaut.security.openid-configuration.enabled", false, "It fetches OpenID Connect metadata at Build time. https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#aotOpenidConfiguration"));
        }
        return optimizations;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }
}
