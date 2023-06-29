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
package io.micronaut.starter.feature.asciidoctor;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.asciidoctor.template.asciidocGradle;
import io.micronaut.starter.feature.asciidoctor.template.asciidocMavenPlugin;
import io.micronaut.starter.feature.asciidoctor.template.indexAdoc;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import jakarta.inject.Singleton;

@Singleton
public class Asciidoctor implements Feature {

    private final CoordinateResolver coordinateResolver;

    public Asciidoctor(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getName() {
        return "asciidoctor";
    }

    @Override
    public String getTitle() {
        return "Asciidoctor Documentation";
    }

    @Override
    public String getDescription() {
        return "Adds support for creating Asciidoctor documentation";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addTemplate("asciidocGradle", new RockerTemplate("gradle/asciidoc.gradle", asciidocGradle.template()));

            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.asciidoctor.jvm.convert")
                    .lookupArtifactId("asciidoctor-gradle-jvm")
                    .build());

        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            String mavenPluginArtifactId = "asciidoctor-maven-plugin";
            BuildProperties props = generatorContext.getBuildProperties();
            coordinateResolver.resolve(mavenPluginArtifactId)
                    .ifPresent(coordinate -> props.put("asciidoctor.maven.plugin.version", coordinate.getVersion()));
            coordinateResolver.resolve("asciidoctorj")
                    .ifPresent(coordinate -> props.put("asciidoctorj.version", coordinate.getVersion()));
            coordinateResolver.resolve("asciidoctorj-diagram")
                    .ifPresent(coordinate -> props.put("asciidoctorj.diagram.version", coordinate.getVersion()));
            coordinateResolver.resolve("jruby")
                    .ifPresent(coordinate -> props.put("jruby.version", coordinate.getVersion()));

            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .artifactId(mavenPluginArtifactId)
                    .extension(new RockerWritable(asciidocMavenPlugin.template()))
                    .build());
        }
        generatorContext.addTemplate("indexAdoc", new RockerTemplate("src/docs/asciidoc/index.adoc", indexAdoc.template()));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DOCUMENTATION;
    }
}
