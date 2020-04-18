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

import io.micronaut.starter.Options;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.gradlekotlin.templates.buildGradleKts;
import io.micronaut.starter.feature.build.gradlekotlin.templates.settingsGradleKts;
import io.micronaut.starter.feature.build.gradlekotlin.templates.versionsKt;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;
import java.util.List;
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
  public void apply(CommandContext commandContext) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    commandContext.addTemplate(
        "gradleWrapperJar",
        new BinaryTemplate(WRAPPER_JAR, classLoader.getResource(String.format("gradleKotlin/%s", WRAPPER_JAR)))
    );
    commandContext.addTemplate(
        "gradleWrapperProperties",
        new URLTemplate(WRAPPER_PROPS, classLoader.getResource(String.format("gradleKotlin/%s", WRAPPER_PROPS)))
    );
    commandContext.addTemplate(
        "gradleWrapper",
        new URLTemplate("gradlew", classLoader.getResource(String.format("gradleKotlin/%s", "gradle/gradlew")), true)
    );
    commandContext.addTemplate(
        "gradleWrapperBat",
        new URLTemplate("gradlew.bat", classLoader.getResource(String.format("gradleKotlin/%s", "gradle/gradlew.bat")), true)
    );

    //buildSrc
    commandContext.addTemplate(
        "buildSrc/build.gradle.kts",
        new URLTemplate("buildSrc/build.gradle.kts", classLoader.getResource(String.format("gradleKotlin/%s", "buildSrc/build.gradle.kts")), true)
    );
    commandContext.addTemplate(
        "buildSrc/settings.gradle.kts",
        new URLTemplate("buildSrc/settings.gradle.kts", classLoader.getResource(String.format("gradleKotlin/%s", "buildSrc/settings.gradle.kts")), true)
    );
    commandContext.addTemplate(
        "versionsKt",
        new RockerTemplate("buildSrc/src/main/kotlin/Versions.kt",
            versionsKt.template(commandContext.getProject(), commandContext.getFeatures())
        )
    );

    commandContext.addTemplate("build",
        new RockerTemplate("build.gradle.kts",
            buildGradleKts.template(commandContext.getProject(), commandContext.getFeatures())
        )
    );
    commandContext.addTemplate(
        "gitignore",
        new RockerTemplate(".gitignore", gitignore.template())
    );
    commandContext.addTemplate(
        "gradleSettings",
        new RockerTemplate("settings.gradle.kts", settingsGradleKts.template(commandContext.getProject()))
    );
  }

  @Override
  public boolean shouldApply(MicronautCommand micronautCommand,
      Options options,
      List<Feature> selectedFeatures) {
    return options.getBuildTool() == BuildTool.gradleKotlin;
  }
}
