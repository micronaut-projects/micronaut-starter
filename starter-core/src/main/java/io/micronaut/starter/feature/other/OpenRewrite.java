/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.other;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.feature.other.template.openrewriteGradlePlugin;
import io.micronaut.starter.feature.other.template.openrewriteMavenPlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.OPENREWRITE;
import static io.micronaut.starter.feature.Category.DEV_TOOLS;
import static io.micronaut.starter.options.BuildTool.MAVEN;
import static io.micronaut.starter.options.Language.JAVA;

@Singleton
public class OpenRewrite implements LanguageSpecificFeature {

    private final CoordinateResolver coordinateResolver;

    public OpenRewrite(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "openrewrite";
    }

    @Override
    public String getTitle() {
        return "OpenRewrite";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds OpenRewrite plugin and Micronaut upgrade recipe";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getBuildTool();
        if (buildTool.isGradle()) {
            addGradlePlugin(generatorContext);
        } else if (buildTool == MAVEN) {
            addMavenPlugin(generatorContext);
        }
    }

    private void addGradlePlugin(GeneratorContext generatorContext) {
        generatorContext.addHelpLink("Rewrite Gradle Plugin", "https://plugins.gradle.org/plugin/org.openrewrite.rewrite");
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id("org.openrewrite.rewrite")
                .lookupArtifactId("plugin")
                .extension(new RockerWritable(openrewriteGradlePlugin.template()))
                .build());

        generatorContext.addDependency(Dependency.builder()
                .groupId("org.openrewrite.recipe")
                .lookupArtifactId("rewrite-micronaut")
                .scope(OPENREWRITE));
    }

    private void addMavenPlugin(GeneratorContext generatorContext) {
        String mavenPluginArtifactId = "rewrite-maven-plugin";
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(openrewriteMavenPlugin.template()))
                .build());
        BuildProperties props = generatorContext.getBuildProperties();
        coordinateResolver.resolve(mavenPluginArtifactId)
                .ifPresent(coordinate -> props.put(
                        "openrewrite.maven.plugin.version", coordinate.getVersion()));
        coordinateResolver.resolve("rewrite-micronaut")
                .ifPresent(coordinate -> props.put("rewrite-micronaut.version", coordinate.getVersion()));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.openrewrite.org/";
    }

    @Override
    public Language getRequiredLanguage() {
        return JAVA;
    }
}
