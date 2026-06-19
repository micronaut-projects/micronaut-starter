/*
 * Copyright 2017-2025 original authors
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

import static io.micronaut.starter.build.dependencies.Scope.ERRORPRONE;
import static io.micronaut.starter.feature.Category.VALIDATION;

import org.jspecify.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.CompilerArgCodeContributingFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.StringTemplate;
import java.util.List;
import io.micronaut.starter.options.BuildTool;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import io.micronaut.starter.rocker.feature.other.template.nullaway;
import static io.micronaut.core.util.StringUtils.TRUE;

@Requires(property = "micronaut.starter.feature.nullaway.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class NullAway implements CompilerArgCodeContributingFeature {

    public static final String NAME = "nullaway";
    private static final List<String> NULLAWAY_MAVEN_JVM_FLAGS = List.of(
            "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
            "--add-opens jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-opens jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED"
    );

    private final Jspecify jspecify;

    public NullAway(Jspecify jspecify) {
        this.jspecify = jspecify;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Jspecify.class)) {
            featureContext.addFeature(jspecify);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "NullAway Annotation";
    }

    @Override
    public String getDescription() {
        return "NullAway: Fast Annotation-Based Null Checking for Java.";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/uber/NullAway/wiki";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return VALIDATION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(gradlePlugin(generatorContext));
            generatorContext.addDependency(nullawayDependency().scope(ERRORPRONE));
            generatorContext.addDependency(errorProneDependency().scope(ERRORPRONE));
        }
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(nullawayDependency().annotationProcessor());
            generatorContext.addDependency(errorProneDependency().annotationProcessor());
            generatorContext.addTemplate("nullaway-maven-jvm-config", new StringTemplate(".mvn/jvm.config", String.join(System.lineSeparator(), NULLAWAY_MAVEN_JVM_FLAGS)));
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

    private static Dependency.Builder nullawayDependency() {
        return Dependency.builder()
                .groupId("com.uber.nullaway")
                .lookupArtifactId("nullaway");
    }

    private static Dependency.Builder errorProneDependency() {
        return Dependency.builder()
                .groupId("com.google.errorprone")
                .lookupArtifactId("error_prone_core");
    }

    @Override
    public List<String> getCompilerArgs(@NonNull GeneratorContext generatorContext) {
        return List.of("-XDcompilePolicy=simple",
                "--should-stop=ifError=FLOW",
                "-Xplugin:ErrorProne -Xep:NullAway:ERROR -XepOpt:NullAway:AnnotatedPackages=" + generatorContext.getProject().getPackageName());
    }
}
