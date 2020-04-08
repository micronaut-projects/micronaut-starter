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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.URLTemplate;
import io.micronaut.starter.feature.build.gradle.templates.*;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class Gradle implements BuildFeature {

    private static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
    private static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";

    @Override
    public String getName() {
        return "gradle";
    }

    @Override
    public void apply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        commandContext.addTemplate("gradleWrapperJar", new BinaryTemplate(WRAPPER_JAR, classLoader.getResource(WRAPPER_JAR)));
        commandContext.addTemplate("gradleWrapperProperties", new URLTemplate(WRAPPER_PROPS, classLoader.getResource(WRAPPER_PROPS)));
        commandContext.addTemplate("gradleWrapper", new URLTemplate("gradlew", classLoader.getResource("gradle/gradlew"), true));
        commandContext.addTemplate("gradleWrapperBat", new URLTemplate("gradlew.bat", classLoader.getResource("gradle/gradlew.bat"), true));

        commandContext.addTemplate("build", new RockerTemplate("build.gradle", buildGradle.template(
                commandContext.getProject(),
                commandContext.getFeatures()
        )));
        commandContext.addTemplate("gitignore", new RockerTemplate(".gitignore", gitignore.template()));
        commandContext.addTemplate("projectProperties", new RockerTemplate("gradle.properties", gradleProperties.template(commandContext.getProjectProperties())));
        commandContext.addTemplate("gradleSettings", new RockerTemplate("settings.gradle", settingsGradle.template(commandContext.getProject())));
    }
}
