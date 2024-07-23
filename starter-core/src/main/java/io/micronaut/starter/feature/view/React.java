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
package io.micronaut.starter.feature.view;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.*;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;

import java.net.MalformedURLException;
import java.net.URL;

@Singleton
public class React implements ViewFeature, MicronautServerDependent {
    public static final String NODE_GRADLE_PLUGIN_VERSION = "7.0.2";
    private static final String ARTIFACT_ID = "micronaut-views-react";
    private static final String[] FRONTEND_FILES = new String[]{
            "package.json",
            "client.js",
            "server.js",
            "webpack.client.js",
            "webpack.server.js",
            "components/App.js"
    };


    @Override
    public String getName() {
        return "views-react";
    }

    @Override
    public String getTitle() {
        return "React SSR";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering of ReactJS components using the GraalJS engine.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://react.dev/reference/react-dom/server";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#react";
    }

    @Override
    public boolean isPreview() {
        // June 2024: Module is brand new, it may still need to change once it's been used in anger.
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        try {
            generatorContext.addDependency(MicronautDependencyUtils.viewsDependency().artifactId(ARTIFACT_ID).compile());

            if (generatorContext.getBuildTool().isGradle()) {
                generatorContext.addDependency(Dependency.builder()
                        .runtime()
                        .groupId(StarterCoordinates.JS_COMMUNITY.getGroupId())
                        .artifactId(StarterCoordinates.JS_COMMUNITY.getArtifactId())
                        .version(StarterCoordinates.JS_COMMUNITY.getVersion())
                        .pom()
                        .build()
                );

                generatorContext.addBuildPlugin(
                        GradlePlugin.builder()
                                .id("com.github.node-gradle.node")
                                .version(NODE_GRADLE_PLUGIN_VERSION)
                                .buildImports("import com.github.gradle.node.npm.task.NpxTask")
                                .build()
                );
            } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
                // We spell out the individual dependencies here because the Starter dependency management code for
                // Maven builds can't express the direct pom dependency needed by Truffle.
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.graalvm.js")
                        .artifactId("js-language")
                        .version(StarterCoordinates.JS_COMMUNITY.getVersion())
                        .runtime()
                );
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.graalvm.truffle")
                        .artifactId("truffle-enterprise")
                        .version(StarterCoordinates.JS_COMMUNITY.getVersion())
                        .runtime()
                );
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.graalvm.truffle")
                        .artifactId("truffle-runtime")
                        .version(StarterCoordinates.JS_COMMUNITY.getVersion())
                        .runtime()
                );
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.graalvm.polyglot")
                        .artifactId("polyglot")
                        .version(StarterCoordinates.JS_COMMUNITY.getVersion())
                        .runtime()
                );

                Coordinate coordinate = generatorContext.resolveCoordinate("frontend-maven-plugin");
                generatorContext.addBuildPlugin(
                        MavenPlugin.builder()
                                .artifactId(StarterCoordinates.FRONTEND_MAVEN_PLUGIN.getArtifactId())
                                .extension(new RockerWritable(mvnPluginReact.template(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion())))
                                .build()
                );
            }

            // Set up the frontend project. These are *not* resources under views/ because they're raw inputs that will
            // be minified and transpiled as part of the build pipeline.
            var ourResourceURL = Thread.currentThread().getContextClassLoader().getResource("views/react").toString();
            for (var fileName : FRONTEND_FILES) {
                generatorContext.addTemplate(
                        fileName,
                        new URLTemplate("src/main/js/" + fileName, new URL(ourResourceURL + "/" + fileName))
                );
            }
            var sourceFile = generatorContext.getSourcePath("/{packagePath}/AppController");

            if (generatorContext.getLanguage() == Language.JAVA) {
                generatorContext.addTemplate("AppController.java",
                        new RockerTemplate(sourceFile, reactControllerJava.template(generatorContext.getProject())));
            } else if (generatorContext.getLanguage() == Language.KOTLIN) {
                generatorContext.addTemplate("AppController.kt",
                        new RockerTemplate(sourceFile, reactControllerKotlin.template(generatorContext.getProject())));
            }

            // This will stop being necessary in Truffle 24.1
            generatorContext.getConfiguration().addNested("micronaut.executors.blocking.virtual", "false");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);   // Cannot happen.
        }
    }
}
