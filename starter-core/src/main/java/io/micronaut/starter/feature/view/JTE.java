/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;

@Singleton
public class JTE implements ViewFeature, MicronautServerDependent {

    @Override
    public String getName() {
        return "views-jte";
    }

    @Override
    public String getTitle() {
        return "JTE Views";
    }

    @Override
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
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.views")
                .lookupArtifactId("micronaut-views-jte")
                .compile());
        String jteSrcDir = jteSrcDir(generatorContext);
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id("gg.jte.gradle")
                .extension(new RockerWritable(gradlePluginJTE.template(jteSrcDir)))
                .lookupArtifactId("jte-gradle-plugin")
                .build());
        String mavenPluginArtifactId = "jte-maven-plugin";
        Coordinate coordinate = generatorContext.resolveCoordinate(mavenPluginArtifactId);
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(mvnPluginJTE.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        jteSrcDir)))
                .build());
        generatorContext.addTemplate("exampleJte", new RockerTemplate(jteSrcDir + "/example.jte", exampleJTE.template()));
    }

    private String jteSrcDir(GeneratorContext generatorContext) {
        String path = generatorContext.getConfiguration().getPath();
        if (path.endsWith("resources/")) {
            path = path.substring(0, path.lastIndexOf("resources/"));
        }
        path = path + "jte";
        return path;
    }
}
