/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.springboot;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.GradleSpecificFeature;
import io.micronaut.starter.feature.build.gradle.Gradle;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.build.gradle.templates.useJunitPlatform;

import java.util.Set;

@Singleton
public class SpringBootGradlePlugin implements GradleSpecificFeature, TestFeature, SpringDefaultFeature {
    private static final String ARTIFACT_ID = "spring-boot-gradle-plugin";

    private final Gradle gradle;

    public SpringBootGradlePlugin(Gradle gradle) {
        this.gradle = gradle;
    }

    @Override
    public String getName() {
        return "springboot-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Boot Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Boot Gradle Plugin provides Spring Boot support in Gradle.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(Gradle.class, gradle);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.springframework.boot")
                    .lookupArtifactId(ARTIFACT_ID)
                    .extension(new RockerWritable(useJunitPlatform.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY))))
                    .build());
        }
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        TestFramework selectedTest = options.getTestFramework();
        if (selectedTest == null) {
            selectedTest = options.getLanguage().getDefaults().getTest();
        }
        return supports(applicationType) && selectedTest == getTestFramework() && options.getBuildTool().isGradle();
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.JUNIT;
    }
}
