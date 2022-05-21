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
package io.micronaut.starter.feature.reloading;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import jakarta.inject.Singleton;

@Singleton
public class Jrebel implements ReloadingFeature {

    @Override
    public String getName() {
        return "jrebel";
    }

    @Override
    public String getTitle() {
        return "JRebel JVM Agent";
    }

    @Override
    public String getDescription() {
        return "Adds support for class reloading with JRebel (requires separate JRebel installation)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.getBuildProperties().addComment("TODO: Replace with agent path from JRebel installation; see documentation");
            generatorContext.getBuildProperties().addComment("rebelAgent=-agentpath:~/bin/jrebel/lib/jrebel6/lib/libjrebel64.dylib");
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.zeroturnaround.gradle.jrebel")
                    .lookupArtifactId("gradle-jrebel-plugin")
                    .build());
            generatorContext.addHelpLink("JRebel Gradle Plugin", "https://plugins.gradle.org/plugin/org.zeroturnaround.gradle.jrebel");
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#jrebel";
    }
}
