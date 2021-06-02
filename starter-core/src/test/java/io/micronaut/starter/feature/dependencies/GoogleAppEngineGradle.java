package io.micronaut.starter.feature.dependencies;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Requires(property = "spec.name", value = "SettingsExtensionsSpec")
@Singleton
public class GoogleAppEngineGradle implements Feature {
    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://github.com/GoogleCloudPlatform/app-gradle-plugin";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Google App Engine Gradle Plugin";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds a Gradle plugin provides tasks to build and deploy Google App Engine applications.";
    }

    @NonNull
    @Override
    public String getName() {
        return "google-app-engine-gradle";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.google.cloud.tools.appengine")
                    .lookupArtifactId("appengine-gradle-plugin")
                    .extension(outputStream -> outputStream.write(String.join("\n", Arrays.asList("appengine {",
                            "    stage.artifact = \"${buildDir}/libs/${project.name}-${project.version}-all.jar\"",
                            "    deploy {",
                            "        projectId = \"changethistoyourprojectid\"",
                            "    }",
                            "}")).getBytes(StandardCharsets.UTF_8)))
                    .settingsExtension(outputStream -> outputStream.write(String.join("\n", Arrays.asList("pluginManagement {",
                                    "    repositories {",
                                    "        gradlePluginPortal()",
                                    "        mavenCentral()",
                                    "    }",
                                    "    resolutionStrategy {",
                                    "        eachPlugin {",
                                    "            if (requested.id.id.startsWith(\"com.google.cloud.tools.appengine\")) {",
                                    "                useModule(\"com.google.cloud.tools:appengine-gradle-plugin:${requested.version}\")",
                                    "            }",
                                    "        }",
                                    "    }",
                                    "}")).getBytes(StandardCharsets.UTF_8)))
                    .build());
        }
    }
}
