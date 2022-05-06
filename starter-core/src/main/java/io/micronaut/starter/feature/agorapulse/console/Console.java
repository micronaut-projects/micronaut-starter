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
package io.micronaut.starter.feature.agorapulse.console;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.starter.feature.agorapulse.console.template.consoleGroovyDsl;
import io.micronaut.starter.feature.agorapulse.console.template.consoleGroovyHttp;
import io.micronaut.starter.feature.agorapulse.console.template.consoleKotlinHttp;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class Console implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-console";

    @Override
    @NonNull
    public String getName() {
        return "micronaut-console";
    }

    @Override
    public String getTitle() {
        return "Micronaut Console";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/micronaut-console/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
        addExampleCode(generatorContext);
    }

    private void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID)
                .runtime());

        if (generatorContext.getLanguage() == Language.JAVA) {
            addGroovyDependency(generatorContext);
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            addKotlinScriptingDependency(generatorContext);
        }
    }

    private void addGroovyDependency(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.getBuildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
        }
        Dependency.Builder groovy = Dependency.builder()
                .groupId("org.codehaus.groovy")
                .artifactId("groovy")
                .runtime();
        generatorContext.addDependency(groovy);
    }

    private void addKotlinScriptingDependency(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate("kotlin-bom");
        generatorContext.getBuildProperties().put("kotlinVersion", coordinate.getVersion());
        Dependency.Builder kotlin = Dependency.builder()
                .groupId("org.jetbrains.kotlin")
                .compile()
                .version("${kotlinVersion}")
                .template();

        generatorContext.addDependency(kotlin.artifactId("kotlin-scripting-jsr223").runtime());
    }

    private void addExampleCode(GeneratorContext generatorContext) {
        addDslFile(generatorContext);
        addHttpFile(generatorContext);
    }

    private void addDslFile(GeneratorContext generatorContext) {
        dslFile(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("consoleGroovyDsl", new RockerTemplate("src/test/resources/console.gdsl", rockerModel));
        });
    }

    private void addHttpFile(GeneratorContext generatorContext) {
        httpFile(generatorContext).ifPresent(rockerModel -> generatorContext.
                addTemplate("consoleHttpFile", new RockerTemplate("src/test/resources/console.http", rockerModel))
        );
    }

    @NonNull
    private Optional<RockerModel> dslFile(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.empty();
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyDsl.template());
    }

    @NonNull
    private Optional<RockerModel> httpFile(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(consoleKotlinHttp.template());
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyHttp.template());
    }


}
