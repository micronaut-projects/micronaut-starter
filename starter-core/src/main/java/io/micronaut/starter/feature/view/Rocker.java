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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class Rocker implements ViewFeature, MicronautServerDependent {

    @Override
    public String getName() {
        return "views-rocker";
    }

    @Override
    public String getTitle() {
        return "Rocker Views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Rocker";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/fizzed/rocker";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#rocker";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.views")
                .artifactId("micronaut-views-rocker")
                .compile());
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id("nu.studer.rocker")
                .extension(new RockerWritable(gradlePluginRocker.template(rockerSrcDir(generatorContext))))
                .lookupArtifactId("gradle-rocker-plugin")
                .build());
        String mavenPluginArtifactId = "rocker-maven-plugin";
        Coordinate coordinate = generatorContext.resolveCoordinate(mavenPluginArtifactId);
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(mvnPluginRocker.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        rockerSrcDir(generatorContext))))
                .build());
    }

    private String rockerSrcDir(GeneratorContext generatorContext) {
        String path = generatorContext.getConfiguration().getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        return path;
    }
}
