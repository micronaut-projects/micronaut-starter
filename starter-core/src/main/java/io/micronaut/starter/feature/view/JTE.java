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
package io.micronaut.starter.feature.view;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.gradle.GradleDsl;
import java.util.Optional;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.feature.build.Kapt;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class JTE implements ViewFeature, MicronautServerDependent {
    private static final String MAVEN_PLUGIN_ARTIFACT_ID = "jte-maven-plugin";

    private static final String JTE_SRC_DIR = "src/main/jte";

    @Override
    @NonNull
    public String getName() {
        return "views-jte";
    }

    @Override
    public String getTitle() {
        return "JTE Views";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using JTE";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://jte.gg/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/#jte";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(dependencyBuilder());
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addBuildPlugin(mavenPlugin(generatorContext));
        }
        generatorContext.addTemplate("exampleJte", new RockerTemplate(JTE_SRC_DIR + "/example.jte", exampleJTE.template()));
    }

    private Dependency.Builder  dependencyBuilder() {
        return Dependency.builder()
                .groupId("io.micronaut.views")
                .artifactId("micronaut-views-jte")
                .compile();
    }

    private BuildPlugin gradlePlugin(GeneratorContext generatorContext) {
        Optional<GradleDsl> gradleDsl = generatorContext.getBuildTool().getGradleDsl();

        boolean patchKapt = generatorContext.getBuildTool().isGradle()
                && generatorContext.getLanguage() == Language.KOTLIN
                && generatorContext.hasFeature(Kapt.class);

        GradlePlugin.Builder builder = GradlePlugin.builder()
                .id("gg.jte.gradle")
                .extension(new RockerWritable(gradlePluginJTE.template(patchKapt, gradleDsl.orElse(GradleDsl.KOTLIN), JTE_SRC_DIR)))
                .lookupArtifactId("jte-gradle-plugin");
        return builder.build();
    }

    private BuildPlugin mavenPlugin(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate(MAVEN_PLUGIN_ARTIFACT_ID);
        return MavenPlugin.builder()
                .artifactId(MAVEN_PLUGIN_ARTIFACT_ID)
                .extension(new RockerWritable(mvnPluginJTE.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        JTE_SRC_DIR)))
                .build();
    }

}
