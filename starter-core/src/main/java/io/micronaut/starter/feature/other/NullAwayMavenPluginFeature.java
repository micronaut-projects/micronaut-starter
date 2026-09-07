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
import io.micronaut.starter.feature.CompilerArgCodeContributingFeature;
import io.micronaut.starter.template.StringTemplate;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static io.micronaut.core.util.StringUtils.TRUE;

@Requires(property = "micronaut.starter.feature.nullaway.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class NullAwayMavenPluginFeature extends NullAway implements CompilerArgCodeContributingFeature {
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

    protected NullAwayMavenPluginFeature(Jspecify jspecify) {
        super(jspecify);
    }

    @Override
    public String getName() {
        return "nullaway-maven-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(nullawayDependency().annotationProcessor());
        generatorContext.addDependency(errorProneDependency().annotationProcessor());
        generatorContext.addTemplate("nullaway-maven-jvm-config", new StringTemplate(".mvn/jvm.config", String.join(System.lineSeparator(), NULLAWAY_MAVEN_JVM_FLAGS)));
    }

    @Override
    public List<String> getCompilerArgs(@NonNull GeneratorContext generatorContext) {
        return List.of("-XDcompilePolicy=simple",
                "--should-stop=ifError=FLOW",
                "-Xplugin:ErrorProne -Xep:NullAway:ERROR -XepOpt:NullAway:AnnotatedPackages=" + generatorContext.getProject().getPackageName());
    }
}
