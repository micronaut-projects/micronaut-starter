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
import io.micronaut.starter.feature.agorapulse.worker.Worker;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class Console implements AgoraPulseFeature {

    protected static final String ARTIFACT_ID = "micronaut-console";
    protected static final String SSRF_HEADER_NAME = "X-Console-Verify";

    @Override
    @NonNull
    public String getCommunityFeatureName() {
        return "micronaut-console";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Micronaut Console";
    }

    @Override
    public String getDescription() {
        return "An extension to Micronaut applications and functions which allows executing arbitrary code.";
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
        String secret = UUID.randomUUID().toString();
        addDependency(generatorContext);
        addExampleCode(generatorContext, secret);
        addConfiguration(generatorContext, secret);
    }

    protected void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID)
                .developmentOnly());

        if (generatorContext.getLanguage() == Language.JAVA) {
            addGroovyDependency(generatorContext);
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            addKotlinScriptingDependency(generatorContext);
        }
    }

    protected void addGroovyDependency(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.getBuildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
        }
        Dependency.Builder groovy = Dependency.builder()
                .groupId("org.apache.groovy")
                .artifactId("groovy")
                .developmentOnly();
        generatorContext.addDependency(groovy);
    }

    protected void addKotlinScriptingDependency(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate("kotlin-bom");
        generatorContext.getBuildProperties().put("kotlinVersion", coordinate.getVersion());
        Dependency.Builder kotlin = Dependency.builder()
                .groupId("org.jetbrains.kotlin")
                .compile()
                .version("${kotlinVersion}")
                .template();

        generatorContext.addDependency(kotlin.artifactId("kotlin-scripting-jsr223").developmentOnly());
    }

    protected void addExampleCode(GeneratorContext generatorContext, String secret) {
        addDslFile(generatorContext);
        addHttpFile(generatorContext, secret);
    }

    protected void addDslFile(GeneratorContext generatorContext) {
        dslFile(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("consoleGroovyDsl", new RockerTemplate("src/test/resources/console.gdsl", rockerModel));
        });
    }

    protected void addHttpFile(GeneratorContext generatorContext, String secret) {
        httpFile(generatorContext, secret).ifPresent(rockerModel -> generatorContext.
                addTemplate("consoleHttpFile", new RockerTemplate("src/test/resources/console.http", rockerModel))
        );
    }

    @NonNull
    protected Optional<RockerModel> dslFile(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.empty();
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyDsl.template(generatorContext.isFeaturePresent(Worker.class)));
    }

    @NonNull
    protected Optional<RockerModel> httpFile(GeneratorContext generatorContext, String secret) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(consoleKotlinHttp.template(SSRF_HEADER_NAME, secret));
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyHttp.template(SSRF_HEADER_NAME, secret));
    }

    protected void addConfiguration(GeneratorContext generatorContext, String secret) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("enabled", true);
        settings.put("addresses", Arrays.asList("/127.0.0.1", "/0:0:0:0:0:0:0:1"));
        settings.put("header-name", SSRF_HEADER_NAME);
        settings.put("header-value", secret);

        generatorContext.getConfiguration().addNested(Collections.singletonMap("console", settings));
    }

}
