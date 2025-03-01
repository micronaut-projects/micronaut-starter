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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.LanguageSpecificFeature;
import io.micronaut.starter.feature.other.template.openrewriteGradlePlugin;
import io.micronaut.starter.feature.other.template.openrewriteMavenPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import static io.micronaut.projectgen.core.buildtools.Scope.OPENREWRITE;
import static io.micronaut.starter.feature.Category.DEV_TOOLS;
import static io.micronaut.projectgen.core.buildtools.BuildTool.MAVEN;
import static io.micronaut.projectgen.core.options.Language.JAVA;

@Requires(property = "micronaut.starter.feature.openrewrite.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
        return "Adds OpenRewrite plugin and Micronaut3to4Migration migration recipe";
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
        generatorContext.addHelpLink("Rewrite Micronaut3to4Migration Recipe", "https://docs.openrewrite.org/running-recipes/popular-recipe-guides/migrate-to-micronaut-4-from-micronaut-3");
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
        generatorContext.addHelpLink("Rewrite Micronaut3to4Migration Recipe", "https://docs.openrewrite.org/running-recipes/popular-recipe-guides/migrate-to-micronaut-4-from-micronaut-3");
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
    public String getCategory() {
        return DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.openrewrite.org/";
    }

    @Override
    public Language getRequiredLanguage() {
        return JAVA;
    }
}
