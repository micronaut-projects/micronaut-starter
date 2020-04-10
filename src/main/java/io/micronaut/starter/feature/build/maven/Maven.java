/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.build.maven;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.maven.templates.pom;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.URLTemplate;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Maven implements BuildFeature {

    private static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    private static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    private static final String WRAPPER_DOWNLOADER = ".mvn/wrapper/MavenWrapperDownloader.java";

    @Override
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        commandContext.addTemplate("mavenWrapperJar", new BinaryTemplate(WRAPPER_JAR, classLoader.getResource("maven/" + WRAPPER_JAR)));
        commandContext.addTemplate("mavenWrapperProperties", new URLTemplate(WRAPPER_PROPS, classLoader.getResource("maven/" + WRAPPER_PROPS)));
        commandContext.addTemplate("mavenWrapperDownloader", new URLTemplate(WRAPPER_DOWNLOADER, classLoader.getResource("maven/" + WRAPPER_DOWNLOADER)));
        commandContext.addTemplate("mavenWrapper", new URLTemplate("mvnw", classLoader.getResource("maven/mvnw"), true));
        commandContext.addTemplate("mavenWrapperBat", new URLTemplate("mvnw.bat", classLoader.getResource("maven/mvnw.cmd"), true));

        commandContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", pom.template(
                commandContext.getProject(),
                commandContext.getFeatures(),
                commandContext.getBuildProperties().getProperties()
        )));
        commandContext.addTemplate("gitignore", new RockerTemplate(".gitignore", gitignore.template()));
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand,
                               Language language,
                               TestFramework testFramework, BuildTool buildTool,
                               List<Feature> selectedFeatures) {
        return buildTool == BuildTool.maven;
    }
}
