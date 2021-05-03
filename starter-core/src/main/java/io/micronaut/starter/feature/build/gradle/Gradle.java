/*
 * Copyright 2017-2020 original authors
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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.gradle.templates.buildGradle;
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties;
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle;
import io.micronaut.starter.feature.database.JpaFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class Gradle implements BuildFeature {
    private static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
    private static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";

    private final GradleBuildCreator dependencyResolver;

    public Gradle(GradleBuildCreator dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public String getName() {
        return "gradle";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        generatorContext.addTemplate("gradleWrapperJar", new BinaryTemplate(WRAPPER_JAR, classLoader.getResource(WRAPPER_JAR)));
        generatorContext.addTemplate("gradleWrapperProperties", new URLTemplate(WRAPPER_PROPS, classLoader.getResource(WRAPPER_PROPS)));
        generatorContext.addTemplate("gradleWrapper", new URLTemplate("gradlew", classLoader.getResource("gradle/gradlew"), true));
        generatorContext.addTemplate("gradleWrapperBat", new URLTemplate("gradlew.bat", classLoader.getResource("gradle/gradlew.bat"), true));

        if (generatorContext.getFeatures().language().isKotlin() || generatorContext.getFeatures().testFramework().isKotlinTestFramework()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.jetbrains.kotlin.jvm").lookupArtifactId("kotlin-gradle-plugin").build());
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.jetbrains.kotlin.kapt").lookupArtifactId("kotlin-gradle-plugin").build());
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.jetbrains.kotlin.plugin.allopen").lookupArtifactId("kotlin-allopen").build());
            if (generatorContext.getFeatures().isFeaturePresent(JpaFeature.class)) {
                generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.jetbrains.kotlin.plugin.jpa").lookupArtifactId("kotlin-noarg").build());
            }
        }
        if (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder().id("groovy").build());
        }
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id(shouldApplyMicronautApplicationGradlePlugin(generatorContext) ? "io.micronaut.application" : "io.micronaut.library")
                .lookupArtifactId("micronaut-gradle-plugin")
                .build());

        BuildTool buildTool = generatorContext.getBuildTool();
        GradleBuild build = dependencyResolver.create(generatorContext);

        generatorContext.addTemplate("build", new RockerTemplate(buildTool.getBuildFileName(), buildGradle.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                build
        )));
        generatorContext.addTemplate("gitignore", new RockerTemplate(".gitignore", gitignore.template()));
        generatorContext.addTemplate("projectProperties", new RockerTemplate("gradle.properties", gradleProperties.template(generatorContext.getBuildProperties().getProperties())));
        String settingsFile = buildTool == BuildTool.GRADLE ? "settings.gradle" : "settings.gradle.kts";
        generatorContext.addTemplate("gradleSettings", new RockerTemplate(settingsFile, settingsGradle.template(generatorContext.getProject())));
    }

    private static boolean shouldApplyMicronautApplicationGradlePlugin(GeneratorContext generatorContext) {
        return generatorContext.getFeatures().mainClass().isPresent() ||
                generatorContext.getFeatures().contains("oracle-function") ||
                generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getFeatures().contains("aws-lambda");
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }
}
