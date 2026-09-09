/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.rocker.feature.other.template.nullaway;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;
import static io.micronaut.starter.build.dependencies.Scope.ERRORPRONE;

@Requires(property = "micronaut.starter.feature.nullaway.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class NullAwayGradlePluginFeature extends NullAway {
    public NullAwayGradlePluginFeature(Jspecify jspecify) {
        super(jspecify);
    }

    @Override
    public String getName() {
        return "nullaway-gradle-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
            generatorContext.addDependency(nullawayDependency().scope(ERRORPRONE));
            generatorContext.addDependency(errorProneDependency().scope(ERRORPRONE));
        }
    }

    private static GradlePlugin gradlePlugin(GeneratorContext generatorContext) {
        GradleDsl dsl = generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY);
        GradlePlugin.Builder builder = GradlePlugin.builder()
                .id("net.ltgt.errorprone")
                .lookupArtifactId("net.ltgt.errorprone.gradle.plugin")
                .extension(new RockerWritable(nullaway.template(dsl, generatorContext.getProject())));
        if (dsl == GradleDsl.KOTLIN) {
            builder.buildImports("import net.ltgt.gradle.errorprone.errorprone");
        }
        return builder.build();
    }
}
