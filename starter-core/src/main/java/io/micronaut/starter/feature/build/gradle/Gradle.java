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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.KotlinBuildPlugins;
import io.micronaut.starter.feature.build.MicronautBuildPlugin;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.gradle.templates.buildGradle;
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties;
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.micronaut.starter.build.Repository.micronautRepositories;

@Singleton
public class Gradle implements BuildFeature {
    public static final boolean DEFAULT_USER_VERSION_CATALOGUE = false;

    private static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
    private static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";
    private final KotlinBuildPlugins kotlinBuildPlugins;
    private final GradleBuildCreator dependencyResolver;
    private final MicronautBuildPlugin micronautBuildPlugin;

    public Gradle(GradleBuildCreator dependencyResolver,
                  MicronautBuildPlugin micronautBuildPlugin,
                  KotlinBuildPlugins kotlinBuildPlugins) {
        this.dependencyResolver = dependencyResolver;
        this.micronautBuildPlugin = micronautBuildPlugin;
        this.kotlinBuildPlugins = kotlinBuildPlugins;
    }

    @Override
    public String getName() {
        return "gradle";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(micronautBuildPlugin);
        if (kotlinBuildPlugins.shouldApply(featureContext)) {
            featureContext.addFeature(kotlinBuildPlugins);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        generatorContext.addTemplate("gradleWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(WRAPPER_JAR)));
        generatorContext.addTemplate("gradleWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(WRAPPER_PROPS)));
        generatorContext.addTemplate("gradleWrapper", new URLTemplate(Template.ROOT, "gradlew", classLoader.getResource("gradle/gradlew"), true));
        generatorContext.addTemplate("gradleWrapperBat", new URLTemplate(Template.ROOT, "gradlew.bat", classLoader.getResource("gradle/gradlew.bat"), DEFAULT_USER_VERSION_CATALOGUE));

        if (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("groovy").build());
        }

        BuildTool buildTool = generatorContext.getBuildTool();
        GradleBuild build = dependencyResolver.create(generatorContext, micronautRepositories(), useVersionCatalog());

        generatorContext.addTemplate("build", new RockerTemplate(buildTool.getBuildFileName(), buildGradle.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                build
        )));

        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitignore.template()));
        generatorContext.addTemplate("projectProperties", new RockerTemplate(Template.ROOT, "gradle.properties", gradleProperties.template(gradleProperties(generatorContext))));

        String settingsFile = buildTool == BuildTool.GRADLE ? "settings.gradle" : "settings.gradle.kts";
        generatorContext.addTemplate("gradleSettings", new RockerTemplate(Template.ROOT, settingsFile, settingsGradle.template(generatorContext.getProject(), build, generatorContext.getModuleNames())));
    }

    /**
     *
     * @return Whether the build should use Micronaut Gradle Version Catalog.
     */
    public boolean useVersionCatalog() {
        return DEFAULT_USER_VERSION_CATALOGUE;
    }

    @NonNull
    private static List<Property> gradleProperties(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getBuildProperties().getProperties().stream()
                .filter(p -> p.getKey() == null || !p.getKey().equals(MicronautRuntimeFeature.PROPERTY_MICRONAUT_RUNTIME)) // It is set via the DSL
                .collect(Collectors.toList());
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }
}
