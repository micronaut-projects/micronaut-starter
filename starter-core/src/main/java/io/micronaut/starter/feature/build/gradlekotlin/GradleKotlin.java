/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.build.gradlekotlin;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.gradlekotlin.templates.buildGradleKts;
import io.micronaut.starter.feature.build.gradlekotlin.templates.settingsGradleKts;
import io.micronaut.starter.feature.build.gradlekotlin.templates.versionsKt;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;
import java.util.Set;
import javax.inject.Singleton;

@Singleton
public class GradleKotlin implements BuildFeature {

  private static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
  private static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";

  @Override
  public String getName() {
    return "gradle-kotlin";
  }

  @Override
  public void apply(GeneratorContext generatorContext) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    generatorContext.addTemplate(
        "gradleWrapperJar",
        new BinaryTemplate(WRAPPER_JAR, classLoader.getResource(String.format("gradleKotlin/%s", WRAPPER_JAR)))
    );
    generatorContext.addTemplate(
        "gradleWrapperProperties",
        new URLTemplate(WRAPPER_PROPS, classLoader.getResource(String.format("gradleKotlin/%s", WRAPPER_PROPS)))
    );
    generatorContext.addTemplate(
        "gradleWrapper",
        new URLTemplate("gradlew", classLoader.getResource(String.format("gradleKotlin/%s", "gradle/gradlew")), true)
    );
    generatorContext.addTemplate(
        "gradleWrapperBat",
        new URLTemplate("gradlew.bat", classLoader.getResource(String.format("gradleKotlin/%s", "gradle/gradlew.bat")), true)
    );

    //buildSrc
    generatorContext.addTemplate(
        "buildSrc/build.gradle.kts",
        new URLTemplate("buildSrc/build.gradle.kts", classLoader.getResource(String.format("gradleKotlin/%s", "buildSrc/build.gradle.kts")), true)
    );
    generatorContext.addTemplate(
        "versionsKt",
        new RockerTemplate("buildSrc/src/main/kotlin/Versions.kt",
            versionsKt.template(generatorContext.getProject(), generatorContext.getFeatures())
        )
    );

    generatorContext.addTemplate("build",
        new RockerTemplate("build.gradle.kts",
            buildGradleKts.template(generatorContext.getProject(), generatorContext.getFeatures())
        )
    );
    generatorContext.addTemplate(
        "gitignore",
        new RockerTemplate(".gitignore", gitignore.template())
    );
    generatorContext.addTemplate(
        "gradleSettings",
        new RockerTemplate("settings.gradle.kts", settingsGradleKts.template(generatorContext.getProject()))
    );
  }

  @Override
  public boolean shouldApply(ApplicationType applicationType,
                             Options options,
                             Set<Feature> selectedFeatures) {
    return options.getBuildTool() == BuildTool.GRADLE_KOTLIN;
  }
}
