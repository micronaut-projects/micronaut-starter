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
package io.micronaut.starter.feature.build.maven;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.DefaultRepositoryResolver;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenBuildCreator;
import io.micronaut.starter.build.maven.MavenRepository;
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
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.build.maven.templates.multimodule;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Singleton
public class Maven implements BuildFeature {
    protected static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    protected static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    protected static final String MAVEN_PREFIX = "maven/";

    protected final MavenBuildCreator dependencyResolver;
    protected final RepositoryResolver repositoryResolver;

    @Deprecated
    public Maven(MavenBuildCreator dependencyResolver) {
        this(dependencyResolver, new DefaultRepositoryResolver());
    }

    @Inject
    public Maven(MavenBuildCreator dependencyResolver, RepositoryResolver repositoryResolver) {
        this.dependencyResolver = dependencyResolver;
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addMavenWrapper(generatorContext);
        addPom(generatorContext);
        addGitIgnore(generatorContext);
        Collection<String> moduleNames = generatorContext.getModuleNames();
        if (moduleNames.size() > 1) {
            List<MavenRepository> mavenRepositories = VersionInfo.getMicronautVersion().endsWith("-SNAPSHOT") ?
                    MavenRepository.listOf(repositoryResolver.resolveRepositories(generatorContext)) :
                    null;
            generatorContext.addTemplate("multi-module-pom", new RockerTemplate(Template.ROOT, generatorContext.getBuildTool().getBuildFileName(), multimodule.template(mavenRepositories, generatorContext.getProject(), moduleNames)));
        }
    }

    protected void addMavenWrapper(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("mavenWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(MAVEN_PREFIX + WRAPPER_JAR)));
        generatorContext.addTemplate("mavenWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(MAVEN_PREFIX + WRAPPER_PROPS)));
        generatorContext.addTemplate("mavenWrapper", new URLTemplate(Template.ROOT, "mvnw", classLoader.getResource(MAVEN_PREFIX + "mvnw"), true));
        generatorContext.addTemplate("mavenWrapperBat", new URLTemplate(Template.ROOT, "mvnw.bat", classLoader.getResource(MAVEN_PREFIX + "mvnw.cmd"), false));

    }

    protected void addPom(GeneratorContext generatorContext) {
        MavenBuild mavenBuild = createBuild(generatorContext);
        generatorContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", pom(generatorContext, mavenBuild)));
    }

    protected MavenBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext, repositoryResolver.resolveRepositories(generatorContext));
    }

    protected RockerModel pom(GeneratorContext generatorContext, MavenBuild mavenBuild) {
        return pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                mavenBuild
        );
    }

    protected void addGitIgnore(GeneratorContext generatorContext) {
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitIgnore(generatorContext)));
    }

    @SuppressWarnings("java:S1172") // Unused parameter for extension
    protected RockerModel gitIgnore(GeneratorContext generatorContext) {
        return gitignore.template();
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {
        return options.getBuildTool() == BuildTool.MAVEN;
    }

    @Override
    public boolean isMaven() {
        return true;
    }
}
