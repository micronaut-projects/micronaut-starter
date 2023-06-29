/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.maven.templates.aot;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
public class MicronautAot implements Feature {

    public static final String FEATURE_NAME_AOT = "micronaut-aot";

    private static final String GRADLE_PLUGIN_ID = "io.micronaut.aot";
    private static final String GRADLE_PLUGIN_ARTIFACT_ID = "micronaut-gradle-plugin";
    private static final int GRADLE_PLUGIN_ORDER = 10;

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AOT;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Build time optimizations to provide faster startup times and smaller binaries.";
    }

    @Override
    @Nullable
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aot/latest/guide/";
    }

    @Override
    public String getTitle() {
        return "Micronaut AOT";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id(GRADLE_PLUGIN_ID)
                    .lookupArtifactId(GRADLE_PLUGIN_ARTIFACT_ID)
                    .order(GRADLE_PLUGIN_ORDER)
                    .build());
        } else {
            BuildProperties buildProperties = generatorContext.getBuildProperties();
            buildProperties.put("micronaut.aot.enabled", StringUtils.TRUE);
            buildProperties.put("micronaut.aot.packageName", generatorContext.getProject().getPackageName() + ".aot.generated");

            generatorContext.addTemplate("aotJitProperties", new RockerTemplate("aot-jar.properties", aot.template("jar")));
            if (generatorContext.isFeaturePresent(GraalVM.class)) {
                generatorContext.addTemplate("aotNativeProperties", new RockerTemplate("aot-native-image.properties", aot.template("native-image")));
            }
        }
    }
}
