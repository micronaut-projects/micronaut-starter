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

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.build.BuildFeature;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class Gradle implements BuildFeature {
    public static final boolean DEFAULT_USER_VERSION_CATALOGUE = false;
    protected static final GradlePlugin GROOVY_GRADLE_PLUGIN = GradlePlugin.builder().id("groovy").build();

    protected static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
    protected static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";

    protected final GradleBuildCreator dependencyResolver;
    protected final RepositoryResolver repositoryResolver;

    public Gradle(GradleBuildCreator dependencyResolver,
                  RepositoryResolver repositoryResolver) {
        this.dependencyResolver = dependencyResolver;
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "gradle";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addGradleInitFiles(generatorContext);
        extraPlugins(generatorContext).forEach(generatorContext::addBuildPlugin);
        GradleBuild build = createBuild(generatorContext);
        addBuildFile(generatorContext, build);
        addGitignore(generatorContext);
        addGradleProperties(generatorContext);
        addSettingsFile(generatorContext, build);
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }

    protected void addGradleInitFiles(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("gradleWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(WRAPPER_JAR)));
        generatorContext.addTemplate("gradleWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(WRAPPER_PROPS)));
        generatorContext.addTemplate("gradleWrapper", new URLTemplate(Template.ROOT, "gradlew", classLoader.getResource("gradle/gradlew"), true));
        generatorContext.addTemplate("gradleWrapperBat", new URLTemplate(Template.ROOT, "gradlew.bat", classLoader.getResource("gradle/gradlew.bat"), false));
    }

    protected List<GradlePlugin> extraPlugins(GeneratorContext generatorContext) {
        return (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) ?
                Collections.singletonList(GROOVY_GRADLE_PLUGIN) : Collections.emptyList();
    }

    protected GradleBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext, repositoryResolver.resolveRepositories(generatorContext), Gradle.DEFAULT_USER_VERSION_CATALOGUE);
    }

    protected void addBuildFile(GeneratorContext generatorContext, GradleBuild build) {
        generatorContext.addTemplate("build", new RockerTemplate(generatorContext.getBuildTool().getBuildFileName(), buildFile(generatorContext, build)));
    }

    protected RockerModel buildFile(GeneratorContext generatorContext, GradleBuild build) {
        return buildGradle.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                build
        );
    }

    protected void addGitignore(GeneratorContext generatorContext) {
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitignore(generatorContext)));
    }

    @SuppressWarnings("java:S1172") // Unused parameter for extensibility
    protected RockerModel gitignore(GeneratorContext generatorContext) {
        return gitignore.template();
    }

    protected void addGradleProperties(GeneratorContext generatorContext) {
        generatorContext.addTemplate("projectProperties", new RockerTemplate(Template.ROOT, "gradle.properties", gradleProperties.template(gradleProperties(generatorContext))));
    }

    @NonNull
    protected List<Property> gradleProperties(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getBuildProperties().getProperties().stream()
                .filter(p -> p.getKey() == null || !p.getKey().equals(MicronautRuntimeFeature.PROPERTY_MICRONAUT_RUNTIME)) // It is set via the DSL
                .collect(Collectors.toList());
    }

    protected void addSettingsFile(GeneratorContext generatorContext, GradleBuild build) {
        boolean hasMultiProjectFeature = generatorContext.getFeatures().hasMultiProjectFeature();
        String settingsFile = generatorContext.getBuildTool() == BuildTool.GRADLE ? "settings.gradle" : "settings.gradle.kts";
        generatorContext.addTemplate("gradleSettings", new RockerTemplate(Template.ROOT, settingsFile, settingsGradle.template(generatorContext.getProject(), build, hasMultiProjectFeature, generatorContext.getModuleNames())));
    }

    @Override
    public boolean isGradle() {
        return true;
    }
}

