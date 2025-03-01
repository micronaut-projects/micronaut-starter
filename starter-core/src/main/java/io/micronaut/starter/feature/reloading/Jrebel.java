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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.build.maven.JvmArgumentsFeature;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Requires(property = "micronaut.starter.feature.jrebel.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Jrebel implements ReloadingFeature, JvmArgumentsFeature {

    private static final String JVM_ARGUMENT_AGENT_PATH = "-agentpath:~/bin/jrebel/lib/jrebel6/lib/libjrebel64.dylib";

    private static final String GROUP_ID_ORG_ZEROTURNAROUND = "org.zeroturnaround";
    private static final String ARTIFACT_ID_JREBEL_MAVEN_PLUGIN = "jrebel-maven-plugin";
    
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
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.getBuildProperties().addComment("TODO: Replace with agent path from JRebel installation; see documentation");
            generatorContext.getBuildProperties().addComment("rebelAgent=" + JVM_ARGUMENT_AGENT_PATH);
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.zeroturnaround.gradle.jrebel")
                    .lookupArtifactId("gradle-jrebel-plugin")
                    .build());
            generatorContext.addHelpLink("JRebel Gradle Plugin", "https://plugins.gradle.org/plugin/org.zeroturnaround.gradle.jrebel");
        } else {
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .groupId(GROUP_ID_ORG_ZEROTURNAROUND)
                    .artifactId(ARTIFACT_ID_JREBEL_MAVEN_PLUGIN)
                    .build());
        }
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://docs.micronaut.io/latest/guide/index.html#jrebel";
    }

    @Override
    public List<String> getJvmArguments() {
        return Collections.singletonList(JVM_ARGUMENT_AGENT_PATH);
    }
}
