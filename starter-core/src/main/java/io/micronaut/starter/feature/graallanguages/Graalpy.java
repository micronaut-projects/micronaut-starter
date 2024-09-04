/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.graallanguages;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.graallanguages.templates.graalPyMavenPlugin;

@Singleton
public class Graalpy implements Feature {

    public static final String NAME = "micronaut-graalpy";

    private static final Dependency MICRONAUT_GRAALPY_DEPENDENCY = MicronautDependencyUtils.graalLanguagesDependency()
            .artifactId(NAME)
            .compile()
            .build();

    private final CoordinateResolver coordinateResolver;

    public Graalpy(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut GraalPy Extension";
    }

    @Override
    public String getDescription() {
        return "Adds support for Python using GraalPy";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        // XXX GP - what category
        return Category.LANGUAGES;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_GRAALPY_DEPENDENCY);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            BuildProperties buildProperties = generatorContext.getBuildProperties();
            String graalPyMavenPluginArtefactId = "graalpy-maven-plugin";
            coordinateResolver.resolve(graalPyMavenPluginArtefactId)
                    .ifPresent(coordinate -> buildProperties.put("graalpy.maven.plugin.version", coordinate.getVersion()));

            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .groupId("org.graalvm.python")
                    .artifactId(graalPyMavenPluginArtefactId)
                    .extension(new RockerWritable(graalPyMavenPlugin.template()))
                    .build());
        }
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://graalvm.org/python";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-graalpy/latest/guide/";
    }

}
