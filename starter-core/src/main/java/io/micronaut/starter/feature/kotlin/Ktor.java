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
package io.micronaut.starter.feature.kotlin;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.KotlinApplicationFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePredicate;
import io.micronaut.starter.feature.KotlinSpecificFeature;
import io.micronaut.starter.feature.kotlin.templates.applicationKotlin;
import io.micronaut.starter.feature.kotlin.templates.homeRouteKotlin;
import io.micronaut.starter.feature.kotlin.templates.jacksonFeatureKotlin;
import io.micronaut.starter.feature.kotlin.templates.nameTransformerKotlin;
import io.micronaut.starter.feature.kotlin.templates.uppercaseTransformerKotlin;
import io.micronaut.starter.feature.server.ThirdPartyServerFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;

@Requires(property = "micronaut.starter.feature.ktor.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Ktor implements KotlinApplicationFeature, ThirdPartyServerFeature, KotlinSpecificFeature {

    public static final String NAME = "ktor";
    public static final String GROUP_ID_IO_KTOR = "io.ktor";
    public static final String ARTIFACT_ID_KTOR_SERVER_NETTY = "ktor-server-netty-jvm";
    public static final String ARTIFACT_ID_KTOR_SERIALIZATION_JACKSON = "ktor-serialization-jackson-jvm";
    public static final String ARTIFACT_ID_KTOR_SERVER_CONTENT_NEGOTIATION = "ktor-server-content-negotiation-jvm";
    private final CoordinateResolver coordinateResolver;

    public Ktor(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && (mnOptions.applicationType() == ApplicationType.DEFAULT);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof Ktor;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("Ktor feature only supports Kotlin");
                }
            });
        }
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Support for using Ktor as the server instead of Micronaut’s native HTTP server";
    }

    @Override
    public String getTitle() {
        return "Ktor";
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        KotlinApplicationFeature.super.apply(generatorContext);

        generatorContext.addTemplate("application", new RockerTemplate("src/main/kotlin/{packagePath}/Application.kt", applicationKotlin.template(generatorContext.getProject())));
        generatorContext.addTemplate("homeRoute", new RockerTemplate("src/main/kotlin/{packagePath}/HomeRoute.kt", homeRouteKotlin.template(generatorContext.getProject())));
        generatorContext.addTemplate("jacksonFeature", new RockerTemplate("src/main/kotlin/{packagePath}/JacksonFeature.kt", jacksonFeatureKotlin.template(generatorContext.getProject())));
        generatorContext.addTemplate("nameTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/NameTransformer.kt", nameTransformerKotlin.template(generatorContext.getProject())));
        generatorContext.addTemplate("uppercaseTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/UppercaseTransformer.kt", uppercaseTransformerKotlin.template(generatorContext.getProject())));

        addDependencies(generatorContext);
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.kotlinDependency()
                .artifactId("micronaut-ktor")
                .compile());

        generatorContext.addDependency(MicronautDependencyUtils.validationDependency()
                .artifactId("micronaut-validation")
                .compile());

        coordinateResolver.resolve(ARTIFACT_ID_KTOR_SERVER_NETTY)
                .map(Coordinate::getVersion)
                .ifPresent(version -> {
                    generatorContext.addDependency(Dependency.builder()
                            .groupId(GROUP_ID_IO_KTOR)
                            .artifactId(ARTIFACT_ID_KTOR_SERVER_NETTY)
                            .version(version)
                            .compile());
                });
        coordinateResolver.resolve(ARTIFACT_ID_KTOR_SERIALIZATION_JACKSON)
                .map(Coordinate::getVersion)
                .ifPresent(version -> {
                    generatorContext.addDependency(Dependency.builder()
                            .groupId(GROUP_ID_IO_KTOR)
                            .artifactId(ARTIFACT_ID_KTOR_SERIALIZATION_JACKSON)
                            .version(version)
                            .compile());
                });
        coordinateResolver.resolve(ARTIFACT_ID_KTOR_SERVER_CONTENT_NEGOTIATION)
                .map(Coordinate::getVersion)
                .ifPresent(version -> {
                    generatorContext.addDependency(Dependency.builder()
                            .groupId(GROUP_ID_IO_KTOR)
                            .artifactId(ARTIFACT_ID_KTOR_SERVER_CONTENT_NEGOTIATION)
                            .version(version)
                            .compile());
                });
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-kotlin/latest/guide/index.html#ktor";
    }
}
