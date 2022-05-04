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
package io.micronaut.starter.feature.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenBuildCreator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.maven.templates.pom;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.build.maven.templates.multimodule;

import java.util.List;
import java.util.Set;

@Singleton
public class Maven implements BuildFeature {
    private static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    private static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    private static final String WRAPPER_DOWNLOADER = ".mvn/wrapper/MavenWrapperDownloader.java";
    private static final String MAVEN_PREFIX = "maven/";

    private final MavenBuildCreator dependencyResolver;

    public Maven(MavenBuildCreator dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        generatorContext.addTemplate("mavenWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(MAVEN_PREFIX + WRAPPER_JAR)));
        generatorContext.addTemplate("mavenWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(MAVEN_PREFIX + WRAPPER_PROPS)));
        generatorContext.addTemplate("mavenWrapperDownloader", new URLTemplate(Template.ROOT, WRAPPER_DOWNLOADER, classLoader.getResource(MAVEN_PREFIX + WRAPPER_DOWNLOADER)));
        generatorContext.addTemplate("mavenWrapper", new URLTemplate(Template.ROOT, "mvnw", classLoader.getResource(MAVEN_PREFIX + "mvnw"), true));
        generatorContext.addTemplate("mavenWrapperBat", new URLTemplate(Template.ROOT, "mvnw.bat", classLoader.getResource(MAVEN_PREFIX + "mvnw.cmd"), false));

        MavenBuild mavenBuild = dependencyResolver.create(generatorContext);
        generatorContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                mavenBuild
        )));
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitignore.template()));


        List<String> moduleNames = generatorContext.getModuleNames();
        if (CollectionUtils.isNotEmpty(moduleNames)) {
            generatorContext.addTemplate("multi-module-pom", new RockerTemplate(Template.ROOT, generatorContext.getBuildTool().getBuildFileName(), multimodule.template(generatorContext.getProject(), moduleNames)));
        }

    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {

        return options.getBuildTool() == BuildTool.MAVEN;
    }
}
