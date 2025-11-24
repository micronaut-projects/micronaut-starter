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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.template.StringTemplate;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;


@Requires(property = "micronaut.starter.feature.nullaway.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class NullAway implements Feature {

    private static final String NULLAWAY_MAVEN_JVM_FLAGS = """
            --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
            --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
            --add-opens jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
            --add-opens jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED""";

    public static final String NAME = "null-away";
    private static final Dependency NULLAWAY_DEPENDENCY =
            Dependency.builder()
                    .groupId("com.uber.nullaway")
                    .artifactId("nullaway")
                    .annotationProcessor()
                    .version("${nullaway.version}")
                    .build();
    private static final Dependency ERRORPRONE_CORE_DEPENDENCY =
            Dependency.builder()
                    .groupId("com.google.errorprone")
                    .artifactId("error_prone_core")
                    .annotationProcessor()
                    .version("${error-prone.version}")
                    .build();

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
    public void apply(GeneratorContext generatorContext){
        if(generatorContext.getBuildTool() == BuildTool.GRADLE) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("net.ltgt.errorprone")
                    .lookupArtifactId("net.ltgt.errorprone.gradle.plugin")
                    .version("4.3.0")
                    .build());
            //todo add condition if Jspecify not present
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.jspecify")
                    .artifactId("jspecify")
                    .compile()
                    .build());
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.uber.nullaway")
                    .artifactId("nullaway")
                    .version("0.12.12")
                    .scope(ERRORPRONE)
                    .build());
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.google.errorprone")
                    .artifactId("error_prone_core")
                    .version("2.44.0")
                    .scope(ERRORPRONE)
                    .build());
        }
        if(generatorContext.getBuildTool() == BuildTool.MAVEN) {
            BuildProperties props = generatorContext.getBuildProperties();
            props.put("error-prone.version", "2.29.2");
            props.put("nullaway.version", "0.12.12");
            generatorContext.addDependency(NULLAWAY_DEPENDENCY);
            generatorContext.addDependency(ERRORPRONE_CORE_DEPENDENCY);
            generatorContext.addTemplate("nullaway-maven-jvm-config", new StringTemplate(".mvn/jvm.config", NULLAWAY_MAVEN_JVM_FLAGS));
        }
    }

}
